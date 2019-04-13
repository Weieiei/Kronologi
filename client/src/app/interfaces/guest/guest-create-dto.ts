import { PhoneNumberDTO } from "../phonenumber/phone-number-dto";

export interface GuestCreateDto {
    firstName: string;
    lastName: string;
    email: string;
    phoneNumber: PhoneNumberDTO;
}
