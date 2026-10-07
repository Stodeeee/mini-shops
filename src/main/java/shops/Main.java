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
    public static void main(String[] args) {

        TemplateEngine templateEngine = TemplateEngine.create(
                new DirectoryCodeResolver(Path.of("src/main/jte")),
                ContentType.Html);

        ProductRepository productRepository = new ProductRepository();

        Javalin app = Javalin.create(config -> {
        //PAGE: CATALOG
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
        //PAGE: CART
            config.routes.get("/cart", ctx -> {
                Map<Integer, Integer> cart = ctx.sessionAttribute("cart");
                List<CartLine> lines = new ArrayList<>();
                int total = 0;
                if(cart != null) {
                    for(Integer productId : cart.keySet()) {
                        Product product = productRepository.findById(productId);
                        if(product != null) {
                            lines.add(new CartLine(product, cart.get(productId)));
                        }
                    }
                }

                for (CartLine line : lines) {
                    total += line.lineTotal();
                }

                StringOutput output = new StringOutput();

                templateEngine.render("cart.jte",Map.of("lines", lines,"total", total), output);
                ctx.result(output.toString());
                ctx.contentType("text/html; charset=utf-8");
            });
        //PAGE: CATALOG Part-(CART)
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
        });
        app.start(8080);
    }
}