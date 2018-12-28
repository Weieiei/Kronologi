import { Appointment } from './Appointment';
import { User } from '../user/User';
import { Service } from '../service/Service';

export class AppointmentDetailed extends Appointment {

    public client: User;
    public employee: User;
    public service: Service;

    public constructor(
        id: number, userId: number, employeeId: number, serviceId: number,
        startTime: Date, endTime: Date, notes: string, status: string,
        createdAt: Date, updatedAt: Date,
        client: User, employee: User, service: Service
    ) {
        super(id, userId, employeeId, serviceId, startTime, endTime, notes, status, createdAt, updatedAt);
        this.client = client;
        this.employee = employee;
        this.service = service;
    }

}
