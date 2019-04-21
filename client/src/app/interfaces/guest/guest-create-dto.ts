import {PhoneNumberDTO} from "../phonenumber/phone-number-dto";
import {BookAppointmentDTO} from "../appointment/book-appointment-dto";

export interface GuestCreateDto {
    firstName: string;
    lastName: string;
    email: string;
    phoneNumber: PhoneNumberDTO;
    appointment: BookAppointmentDTO;
}
