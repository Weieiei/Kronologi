import { PhoneNumberDTO } from "../phonenumber/phone-number-dto";

export interface BusinessUserRegisterDTO {
    firstName: string;
    lastName: string;
    email: string;
    password: string;
    phoneNumber: PhoneNumberDTO;
    // businessId: number;
}
