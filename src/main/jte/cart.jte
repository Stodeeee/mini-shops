@import java.util.List
@import shops.CartLine

@param List<CartLine> lines
@param int total
@param String error
<!DOCTYPE html>

<html lang="ru">
<head>
    <script src="https://cdn.jsdelivr.net/npm/htmx.org@2.0.11/dist/htmx.min.js"
            integrity="sha384-2OatzQy1H+Zd/IIrjr1TcuDGqLXeHhbooAyJY1KdQMKnr4LZ22k31GBLdYKHmVjg"
            crossorigin="anonymous">
    </script>
    <meta charset="UTF-8">
    <title>Корзина</title>
</head>

<body>
    <h1>Корзина</h1>
    @if(!error.isEmpty())
        <p style="color: red">${error}</p>
    @endif
    <div id="cart-table">
        @template.cartTable(lines = lines, total = total)
    </div>
    <form action="/orders" method="post">
        <label for="email">Email: </label>
        <input type="email" name="email" id="email" required/>
        <input type="submit" value="Оформить заказ">
    </form>
    <a href="/"><span>Назад в каталог</span></a>

</body>
</html>