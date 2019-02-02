
import { User } from './user';
import { Appointment } from './appointment';

export interface Review {
    review: string;
    client: User;
    appointment: Appointment;
}
