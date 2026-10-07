package gg.jte.generated.ondemand;
import java.util.List;
import shops.Product;
@SuppressWarnings("unchecked")
@javax.annotation.processing.Generated("gg.jte.TemplateEngine")
public final class JtecatalogGenerated {
	public static final String JTE_NAME = "catalog.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,3,3,3,3,3,20,20,20,20,22,22,24,24,24,25,25,25,26,26,26,26,28,28,31,31,31,3,4,4,4,4};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, List<Product> products, int cartCount) {
		jteOutput.writeContent("\r\n<!DOCTYPE html>\r\n\r\n<html lang=\"ru\">\r\n    <head>\r\n        <script src=\"https://cdn.jsdelivr.net/npm/htmx.org@2.0.11/dist/htmx.min.js\"\r\n                integrity=\"sha384-2OatzQy1H+Zd/IIrjr1TcuDGqLXeHhbooAyJY1KdQMKnr4LZ22k31GBLdYKHmVjg\"\r\n                crossorigin=\"anonymous\">\r\n        </script>\r\n        <meta charset=\"UTF-8\">\r\n        <title>Мини-магазин</title>\r\n    </head>\r\n\r\n    <body>\r\n        <h1>Каталог</h1>\r\n        <a href=\"/cart\"><span id=\"cart-info\">Корзина (");
		jteOutput.setContext("span", null);
		jteOutput.writeUserContent(cartCount);
		jteOutput.writeContent(")</span></a>\r\n        <ul>\r\n            ");
		for (Product p : products) {
			jteOutput.writeContent("\r\n            <li>\r\n                ");
			jteOutput.setContext("li", null);
			jteOutput.writeUserContent(p.name());
			jteOutput.writeContent(":\r\n                ");
			jteOutput.setContext("li", null);
			jteOutput.writeUserContent(p.priceCents() / 100);
			jteOutput.writeContent(" ₸\r\n                <button hx-post=\"/cart/add/");
			jteOutput.setContext("button", "hx-post");
			jteOutput.writeUserContent(p.id());
			jteOutput.setContext("button", null);
			jteOutput.writeContent("\" hx-target=\"#cart-info\">Добавить в корзину</button>\r\n            </li>\r\n            ");
		}
		jteOutput.writeContent("\r\n        </ul>\r\n    </body>\r\n</html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		List<Product> products = (List<Product>)params.get("products");
		int cartCount = (int)params.get("cartCount");
		render(jteOutput, jteHtmlInterceptor, products, cartCount);
	}
}
