@import java.util.List
@import shops.OrderEvent
@import shops.Order

@param List<OrderEvent> events
    @param Order order

    <!DOCTYPE html>
    <html lang="ru">
    <head>
        <script src="https://cdn.jsdelivr.net/npm/htmx.org@2.0.11/dist/htmx.min.js"
                integrity="sha384-2OatzQy1H+Zd/IIrjr1TcuDGqLXeHhbooAyJY1KdQMKnr4LZ22k31GBLdYKHmVjg"
                crossorigin="anonymous">
        </script>
        <meta charset="UTF-8">
        <title>Заказ №${order.id()}</title>
    </head>

    <body>
    <h1>Заказ №${order.id()}</h1>
    <div id="order-status" hx-get="/orders/${order.id()}/status" hx-trigger="every 2s" hx-swap="innerHTML">
        @template.orderStatus(events = events, order = order)
    </div>
    <a href="/"><span>Назад в каталог</span></a>
    </body>
    </html>