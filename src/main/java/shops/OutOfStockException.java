package shops;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException(String productName) {
        super("Нет в наличии" + productName);
    }
}
