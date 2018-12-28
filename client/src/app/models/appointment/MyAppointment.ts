import { Appointment } from './Appointment';
import { User } from '../user/User';
import { Service } from '../service/Service';

export class MyAppointment extends Appointment {

    private employee: User;
    private service: Service;

    public constructor(
        id: number, userId: number, employeeId: number, serviceId: number,
        startTime: Date, endTime: Date, notes: string, status: string,
        createdAt: Date, updatedAt: Date,
        employee: User, service: Service
    ) {
        super(id, userId, employeeId, serviceId, startTime, endTime, notes, status, createdAt, updatedAt);
        this.employee = employee;
        this.service = service;
    }

    public getEmployee(): User {
        return this.employee;
    }

    public setEmployee(employee: User): void {
        this.employee = employee;
    }

    public getService(): Service {
        return this.service;
    }

    public setService(service: Service): void {
        this.service = service;
    }

}
