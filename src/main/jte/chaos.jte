@import shops.PaymentMode

@param PaymentMode paymentMode

<!DOCTYPE html>
<html lang="ru">
<head>
    <script src="https://cdn.jsdelivr.net/npm/htmx.org@2.0.11/dist/htmx.min.js"
            integrity="sha384-2OatzQy1H+Zd/IIrjr1TcuDGqLXeHhbooAyJY1KdQMKnr4LZ22k31GBLdYKHmVjg"
            crossorigin="anonymous">
    </script>
    <meta charset="UTF-8">
    <title>Админ Комната</title>
</head>

<body>
<h1>Админ Комната</h1>
<div id="chaos-mode">
    @template.chaosMode(paymentMode = paymentMode)
</div>
<a href="/"><span>Назад в каталог</span></a>
</body>
</html>