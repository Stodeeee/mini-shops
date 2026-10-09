package shops;

import java.util.Set;

public record Order (int id, String email, String status, int totalCents, String createdAt){

private static final Set<String> FINAL_STATUSES = Set.of(
        "COMPLETED", "CANCELLED", "OUT_OF_STOCK", "PAYMENT_FAILED", "PAYMENT_TIMEOUT");

    public boolean isFinal() {
        return FINAL_STATUSES.contains(status);
    }
}
