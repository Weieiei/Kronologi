import {Time} from "@angular/common";
import {User} from "./user";
import {Service} from "./service";

export interface Shift {
    startTime: Time;
    endTime: Time;
    date: Date;
    employee: User;
}
