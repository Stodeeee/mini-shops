package shops;

public class CardDeclinedException extends RuntimeException {
    public CardDeclinedException(int orderId) {
        super("Карта отклонена " + orderId);
    }
}
