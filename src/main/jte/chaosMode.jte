@import shops.PaymentMode

@param PaymentMode paymentMode

<span>Сейчас: ${paymentMode.name()}</span>
<button
    hx-post="/admin/chaos/OK"
    hx-target="#chaos-mode"
    hx-swap="innerHTML">
        OK
</button>

<button
    hx-post="/admin/chaos/DECLINED"
    hx-target="#chaos-mode"
    hx-swap="innerHTML">
        DECLINED
</button>

<button
    hx-post="/admin/chaos/NOT_AVAILABLE"
    hx-target="#chaos-mode"
    hx-swap="innerHTML">
        NOT AVAILABLE
</button>