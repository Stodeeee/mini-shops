@import java.util.List
@import shops.CartLine

@param List<CartLine> lines
@param int total

<div class="table-cart">
    <table class="table">
        <thead>
        <tr>
            <th>Название</th>
            <th>Количество</th>
            <th>Сумма по строке</th>
            <th>Добавить</th>
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
                <td><button hx-post="/cart/inc/${line.product().id()}"
                            hx-target="#cart-table"
                            hx-swap="innerHTML">
                                +
                </button></td>
                <td><button hx-post="/cart/dec/${line.product().id()}"
                            hx-target="#cart-table"
                            hx-swap="innerHTML">
                                -
                    </button></td>
                <td><button hx-post="/cart/remove/${line.product().id()}"
                            hx-target="#cart-table"
                            hx-swap="innerHTML">
                                Удалить
                    </button></td>
            </tr>
        @endfor

        </tbody>
    </table>
    <h3>Итоговая сумма: ${total / 100} ₸</h3>
</div>