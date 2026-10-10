@import java.util.List
@import shops.OrderEvent
@import shops.Order

@param List<OrderEvent> events
@param Order order

<p>Email: ${order.email()}</p>
<p>Статус: <b>${order.status()}</b></p>
<p>Сумма: ${order.totalCents() / 100} ₸</p>
<p>Создан: ${order.createdAt()}</p>

<h3>История</h3>
<ul>
    @for(OrderEvent event : events)
        <li>${event.createdAt()}: ${event.message()}</li>
    @endfor
</ul>
@if(order.status().equals("CREATED"))
    <button hx-post="/orders/${order.id()}/pay"
            hx-target="#order-status"
            hx-swap="innerHTML">
        Оплатить
    </button>
@endif