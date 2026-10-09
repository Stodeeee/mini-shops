package shops;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import gg.jte.resolve.DirectoryCodeResolver;
import io.javalin.Javalin;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    static int cartTotal(Map<Integer, Integer> cart) {
        int total = 0;
        for(int qty : cart.values()) {
            total += qty;
        }
        return total;
    }
    static List<CartLine> collectCartLines(Map<Integer, Integer> cart, ProductRepository productRepository) {
        List<CartLine> lines = new ArrayList<>();
        if(cart != null) {
            for(Integer productId : cart.keySet()) {
                Product product = productRepository.findById(productId);
                if(product != null) {
                    lines.add(new CartLine(product, cart.get(productId)));
                }
            }
        }
        return lines;
    }

    public static void main(String[] args) {

        TemplateEngine templateEngine = TemplateEngine.create(
                new DirectoryCodeResolver(Path.of("src/main/jte")),
                ContentType.Html);

        OrderRepository orderRepository = new OrderRepository();
        ProductRepository productRepository = new ProductRepository();

        Javalin app = Javalin.create(config -> {
//--------------PAGE: CATALOG---------------
            config.routes.get("/", ctx -> {
                List<Product> products = productRepository.findAll();

                Map<Integer,Integer> cart = ctx.sessionAttribute("cart");
                int total = 0;
                if (cart != null) {
                    total = cartTotal(cart);
                }

                StringOutput output = new StringOutput();
                templateEngine.render("catalog.jte",
                        Map.of("products", products, "cartCount", total), output);

                ctx.contentType("text/html; charset=utf-8");
                ctx.result(output.toString());
            });

//--------------PAGE: CATALOG Part-(CART)---------------
            config.routes.post("/cart/add/{id}", ctx ->{

                Integer count = ctx.sessionAttribute("count");
                if(count == null){
                    count = 0;
                }
                count++;
                ctx.sessionAttribute("count", count);

                int id = Integer.parseInt(ctx.pathParam("id"));
                Map<Integer, Integer> cart = ctx.sessionAttribute("cart");
                if(cart == null){
                    cart = new HashMap<>();
                    ctx.sessionAttribute("cart", cart);
                }
                cart.merge(id, 1, Integer::sum);
                int total= cartTotal(cart);

                ctx.result("Корзина (" + total + ")");
                ctx.contentType("text/html; charset=utf-8");
            });
//--------------PAGE: CART----------------------------
            config.routes.get("/cart", ctx -> {

                Map<Integer, Integer> cart = ctx.sessionAttribute("cart");

                List<CartLine> lines = collectCartLines(cart,productRepository);
                int total = 0;
                for (CartLine line : lines) {
                    total += line.lineTotal();
                }

                String error = ctx.sessionAttribute("cartError");
                ctx.sessionAttribute("cartError", null);
                if (error == null) {
                    error = "";
                }

                StringOutput output = new StringOutput();

                templateEngine.render("cart.jte",Map.of("lines", lines,"total", total, "error", error), output);
                ctx.result(output.toString());
                ctx.contentType("text/html; charset=utf-8");
            });
//--------------PAGE: CART Part-(INC)--------------
            config.routes.post("/cart/inc/{id}", ctx -> {

                int id = Integer.parseInt(ctx.pathParam("id"));

                Map<Integer, Integer> cart = ctx.sessionAttribute("cart");

                if(cart == null){
                    cart = new HashMap<>();
                    ctx.sessionAttribute("cart", cart);
                }
                cart.merge(id, 1, Integer::sum);

                List<CartLine> lines = collectCartLines(cart,productRepository);

                int total = 0;
                for (CartLine line : lines) {
                    total += line.lineTotal();
                }
                StringOutput output = new StringOutput();

                templateEngine.render("cartTable.jte",Map.of("lines", lines,"total", total), output);
                ctx.result(output.toString());
                ctx.contentType("text/html; charset=utf-8");
            });
//--------------PAGE: CART Part-(DEC)--------------
            config.routes.post("/cart/dec/{id}", ctx -> {

                int id = Integer.parseInt(ctx.pathParam("id"));

                Map<Integer, Integer> cart = ctx.sessionAttribute("cart");

                if(cart == null){
                    cart = new HashMap<>();
                    ctx.sessionAttribute("cart", cart);
                }
                cart.computeIfPresent(id, (key, qty) -> qty > 1 ? qty - 1 : null);

                List<CartLine> lines = collectCartLines(cart,productRepository);

                int total = 0;
                for (CartLine line : lines) {
                    total += line.lineTotal();
                }
                StringOutput output = new StringOutput();

                templateEngine.render("cartTable.jte",Map.of("lines", lines,"total", total), output);
                ctx.result(output.toString());
                ctx.contentType("text/html; charset=utf-8");
            });
//--------------PAGE: CART Part-(DEL)--------------
            config.routes.post("/cart/remove/{id}", ctx -> {

                int id = Integer.parseInt(ctx.pathParam("id"));

                Map<Integer, Integer> cart = ctx.sessionAttribute("cart");

                if(cart == null){
                    cart = new HashMap<>();
                    ctx.sessionAttribute("cart", cart);
                }
                cart.remove(id);

                List<CartLine> lines = collectCartLines(cart,productRepository);

                int total = 0;
                for (CartLine line : lines) {
                    total += line.lineTotal();
                }
                StringOutput output = new StringOutput();

                templateEngine.render("cartTable.jte",Map.of("lines", lines,"total", total), output);
                ctx.result(output.toString());
                ctx.contentType("text/html; charset=utf-8");
            });
//--------------PAGE: CART Part-(Submit Order)--------------
            config.routes.post("/orders", ctx -> {
                String email = ctx.formParam("email");
                Map<Integer, Integer> cart = ctx.sessionAttribute("cart");

                List<CartLine> lines = collectCartLines(cart,productRepository);
                if(lines.isEmpty()){
                    ctx.redirect("/");
                    return;
                }
                if (email == null) {
                    ctx.redirect("/cart");
                    return;
                }

                int orderId;
                try {
                    orderId = orderRepository.createOrder(email, lines);
                } catch (OutOfStockException e) {
                    ctx.sessionAttribute("cartError", e.getMessage());
                    ctx.redirect("/cart");
                    return;
                }
                ctx.sessionAttribute("cart", null);
                ctx.redirect("/orders/" + orderId);
            });
//------------------------PAGE: ORDER-----------------
            config.routes.get("/orders/{id}", ctx -> {
                int id = Integer.parseInt(ctx.pathParam("id"));

                Order order = orderRepository.findById(id);
                if(order == null){
                    ctx.status(404);
                    return;
                }
                List<OrderEvent> event = orderRepository.findEvents(id);
                System.out.println("events = " + event);
                StringOutput output = new StringOutput();
                templateEngine.render("order.jte", Map.of("order", order, "events", event), output);
                ctx.result(output.toString());
                ctx.contentType("text/html; charset=utf-8");
            });

//------------------------PAGE: ORDER Part-(Status)-----------------
            config.routes.get("/orders/{id}/status", ctx -> {
                int id = Integer.parseInt(ctx.pathParam("id"));

                Order order = orderRepository.findById(id);
                if (order == null) {
                    ctx.status(404);
                    return;
                }
                List<OrderEvent> events = orderRepository.findEvents(id);

                StringOutput output = new StringOutput();
                templateEngine.render("orderStatus.jte", Map.of("order", order, "events", events), output);

                ctx.contentType("text/html; charset=utf-8");
                ctx.result(output.toString());

                if (order.isFinal()) {
                    ctx.status(286);
                }
            });
        });
        app.start(8080);
    }
}