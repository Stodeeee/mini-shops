@import java.util.List
@import shops.Product

@param List<Product> products

<!DOCTYPE html>

<html lang="ru">
    <head>
        <meta charset="UTF-8">
        <title>Мини-магазин</title>
    </head>

    <body>
        <h1>Каталог</h1>
        <ul>
            @for(Product p : products)
            <li>${p.name()}: ${p.priceCents() / 100} ₸</li>
            @endfor
        </ul>
    </body>
</html>