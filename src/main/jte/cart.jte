@import java.util.List
@import shops.CartLine

@param List<CartLine> lines
@param int total
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
<div class="table-cart">
    <table class="table">
        <thead>
            <tr>
                <th>Название</th>
                <th>Количество</th>
                <th>Сумма по строке</th>
            </tr>
        </thead>

        <tbody>
            @for(CartLine line : lines)
            <tr>
                <td>
                    ${line.product().name()}
                </td>
                <td>
                    ${line.qty()}
                </td>
                <td>
                    ${line.lineTotal()/100} ₸
                </td>
            </tr>
            @endfor
            <tr>
            </tr>
        </tbody>
    </table>
    <h3>Итоговая сумма: ${total / 100} ₸</h3>
</div>
    <a href="/"><span>Назад в каталог</span></a>
</body>
</html>