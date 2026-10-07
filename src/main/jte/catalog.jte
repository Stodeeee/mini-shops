@import java.util.List
@import shops.Product

@param List<Product> products
@param int cartCount

<!DOCTYPE html>

<html lang="ru">
    <head>
        <script src="https://cdn.jsdelivr.net/npm/htmx.org@2.0.11/dist/htmx.min.js"
                integrity="sha384-2OatzQy1H+Zd/IIrjr1TcuDGqLXeHhbooAyJY1KdQMKnr4LZ22k31GBLdYKHmVjg"
                crossorigin="anonymous">
        </script>
        <meta charset="UTF-8">
        <title>Мини-магазин</title>
    </head>

    <body>
        <h1>Каталог</h1>
        <a href="/cart"><span id="cart-info">Корзина (${cartCount})</span></a>
        <ul>
            @for(Product p : products)
            <li>
                ${p.name()}:
                ${p.priceCents() / 100} ₸
                <button hx-post="/cart/add/${p.id()}" hx-target="#cart-info">Добавить в корзину</button>
            </li>
            @endfor
        </ul>
    </body>
</html>