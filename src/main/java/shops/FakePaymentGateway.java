package shops;

public class FakePaymentGateway {

    public static volatile PaymentMode paymentMode = PaymentMode.OK;

    static PaymentMode getPaymentMode(){
        return paymentMode;
    }

    static void setPaymentMode(PaymentMode paymentMode) {
        FakePaymentGateway.paymentMode = paymentMode;
    }

    static String charge(int orderId, int sum){
        if(paymentMode == PaymentMode.OK){
            String answer = "pay-" + orderId;
            return answer;
        }
        else if(paymentMode == PaymentMode.DECLINED){
            throw new CardDeclinedException(orderId);
        }
        else if(paymentMode == PaymentMode.NOT_AVAILABLE) {
            throw new GatewayUnavailableException("Платёжный шлюз временно недоступен");
        }
        else{
            throw new IllegalStateException("GG");
        }

    }

}