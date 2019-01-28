import { PhoneNumberDTO } from "../phonenumber/phone-number-dto";

export interface UserRegisterDTO {
    firstName: string;
    lastName: string;
    email: string;
    password: string;
    phoneNumber: PhoneNumberDTO;
}
