import { Time } from '@angular/common';
import { User } from './user';
import { Service } from './service/service';

export interface Appointment {
    email: string;
    startTime: Time;
    endTime: Time;
    date: Date;
    notes: string;
    status: string;
    client: User;
    employee: User;
    service: Service;
    firstName: string
    lastName: string
}
