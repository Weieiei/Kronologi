import { Time } from '@angular/common';
import { User } from './user';

export interface Shift {
    startTime: Time;
    endTime: Time;
    date: Date;
    employee: User;
}
