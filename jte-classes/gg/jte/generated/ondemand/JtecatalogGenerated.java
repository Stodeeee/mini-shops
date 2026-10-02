package gg.jte.generated.ondemand;
import java.util.List;
import shops.Product;
@SuppressWarnings("unchecked")
@javax.annotation.processing.Generated("gg.jte.TemplateEngine")
public final class JtecatalogGenerated {
	public static final String JTE_NAME = "catalog.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,3,3,3,3,3,16,16,16,17,17,17,17,17,17,18,18,21,21,21,3,3,3,3};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, List<Product> products) {
		jteOutput.writeContent("\r\n<!DOCTYPE html>\r\n\r\n<html lang=\"ru\">\r\n    <head>\r\n        <meta charset=\"UTF-8\">\r\n        <title>Мини-магазин</title>\r\n    </head>\r\n\r\n    <body>\r\n        <h1>Каталог</h1>\r\n        <ul>\r\n            ");
		for (Product p : products) {
			jteOutput.writeContent("\r\n            <li>");
			jteOutput.setContext("li", null);
			jteOutput.writeUserContent(p.name());
			jteOutput.writeContent(": ");
			jteOutput.setContext("li", null);
			jteOutput.writeUserContent(p.priceCents() / 100);
			jteOutput.writeContent(" ₸</li>\r\n            ");
		}
		jteOutput.writeContent("\r\n        </ul>\r\n    </body>\r\n</html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		List<Product> products = (List<Product>)params.get("products");
		render(jteOutput, jteHtmlInterceptor, products);
	}
}
