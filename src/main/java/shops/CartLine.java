package shops;

public record CartLine(Product product, int qty) {
    public int lineTotal() {
        return product.priceCents() * qty;
    }
}
