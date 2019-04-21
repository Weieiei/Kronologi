import { PhoneNumberDTO } from "../phonenumber/phone-number-dto";
import { StripeToken } from 'stripe-angular';

export interface StripeUserInfo {
    firstName: string;
    lastName: string;
    birthYear: number;
    birthMonth: number;
    birthDay: number;
    socialInsuranceNumber: number;
    
    address: string;
    city: string;
    province: string;
    country: string;
    postalCode: string;

    businessTaxNumber:number;
    business_type:string;

    tosAcceptance:Date;

    stripeTokenForBankAccount : StripeToken
    
    // businessId: number;
}
