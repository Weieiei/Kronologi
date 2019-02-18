import { Appointment } from './Appointment';
import { User } from '../user/User';
import { Service } from '../service/Service';
import { Time } from '@angular/common';

export class AppointmentDetailed extends Appointment {

    public client: User;
    public employee: User;
    public service: Service;

    public constructor(
        id: number, clientId: number, employeeId: number, serviceId: number,
        startTime: Time, endTime: Time, date: Date, notes: string, status: string,
        createdAt: Date, updatedAt: Date,
        client: User, employee: User, service: Service
    ) {
        super(id, clientId, employeeId, serviceId, startTime, endTime, date, notes, status, createdAt, updatedAt);
        this.client = client;
        this.employee = employee;
        this.service = service;
    }

}
