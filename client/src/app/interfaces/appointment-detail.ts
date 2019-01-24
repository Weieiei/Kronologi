
import { Time } from '@angular/common';
import { User } from './user';
import { Service } from './service';
export interface AppointmentDetail {
    email: string;
    startTime: Time;
    endTime: Time;
    date: Date;
    notes: string;
    status: string;
    client: User;
    employee: User;
    service: Service;
}
