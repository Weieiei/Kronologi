import { PhoneNumberDTO } from "../phonenumber/phone-number-dto";
import { BusinessDTO } from '../business/business-dto';

export interface UserRegisterDTO {
    firstName: string;
    lastName: string;
    email: string;
    password: string;
    phoneNumber: PhoneNumberDTO;
    // businessId: BusinessDTO;
}
