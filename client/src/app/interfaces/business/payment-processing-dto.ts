import { StripeToken } from 'stripe-angular';

export interface PaymentInfoDTO {
    businessId: number;
    servicePrice: number;
    stripeToken: StripeToken;
}
