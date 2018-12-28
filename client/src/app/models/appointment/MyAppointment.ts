import { User } from '../user/User';
import { Service } from '../service/Service';

export class MyAppointment {

    public id: number;
    public userId: number;
    public employeeId: number;
    public serviceId: number;
    public startTime: Date;
    public endTime: Date;
    public notes: string;
    public status: string;
    public employee: User;
    public service: Service;

    public constructor(
        id: number, userId: number, employeeId: number, serviceId: number,
        startTime: Date, endTime: Date, notes: string, status: string,
        createdAt: Date, updatedAt: Date,
        employee: User, service: Service
    ) {
        this.id = id;
        this.userId = userId;
        this.employeeId = employeeId;
        this.serviceId = serviceId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.notes = notes;
        this.status = status;
        this.employee = employee;
        this.service = service;
    }

}
