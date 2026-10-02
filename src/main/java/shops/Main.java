package shops;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import gg.jte.resolve.DirectoryCodeResolver;
import io.javalin.Javalin;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        TemplateEngine templateEngine = TemplateEngine.create(
                new DirectoryCodeResolver(Path.of("src/main/jte")),
                ContentType.Html);

        Javalin app = Javalin.create(config -> {
            config.routes.get("/", ctx -> {
                List<Product> products = List.of(
                        new Product(1, "Футболка", 500000),
                        new Product(2, "Кружка", 150000),
                        new Product(3, "Рюкзак", 1200000));

                StringOutput output = new StringOutput();
                templateEngine.render("catalog.jte", Map.of("products", products), output);

                ctx.contentType("text/html; charset=utf-8");
                ctx.result(output.toString());
            });
        });

        app.start(8080);
    }
}