package appointmentscheduler.dto.appointment;

import com.stripe.model.Token;

public class PaymentInfoDTO {
    private long servicePrice;
    private long businessId;
    private Token stripeToken;

    public PaymentInfoDTO() {
    }

    public PaymentInfoDTO(Token stripeToken, long servicePrice, long businessId) {
        this.stripeToken = stripeToken;
        this.servicePrice = servicePrice;
        this.businessId = businessId;
    }

    public long getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(long servicePrice) {
        this.servicePrice = servicePrice;
    }

    public long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(long businessId) {
        this.businessId = businessId;
    }

    public Token getStripeToken() {
        return stripeToken;
    }

    public void setStripeToken(Token stripe_token) {
        this.stripeToken = stripe_token;
    }
}
