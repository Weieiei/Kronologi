import { Appointment } from './Appointment';
import { User } from '../user/User';
import { Service } from '../service/Service';

export class MyAppointment extends Appointment {

    public employee: User;
    public service: Service;

    public constructor(
        id: number, clientId: number, employeeId: number, serviceId: number,
        startTime: Date, endTime: Date, notes: string, status: string,
        createdAt: Date, updatedAt: Date,
        employee: User, service: Service
    ) {
        super(id, clientId, employeeId, serviceId, startTime, endTime, notes, status, createdAt, updatedAt);
        this.employee = employee;
        this.service = service;
    }

}
