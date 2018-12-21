import { Appointment } from './Appointment';
import { User } from '../user/User';
import { Service } from '../service/Service';

export class AppointmentDetailed extends Appointment {

    private client: User;
    private employee: User;
    private service: Service;

    public constructor(
        id: number, userId: number, employeeId: number, serviceId: number,
        startTime: Date, endTime: Date, notes: string,
        createdAt: Date, updatedAt: Date,
        client: User, employee: User, service: Service
    ) {
        super(id, userId, employeeId, serviceId, startTime, endTime, notes, createdAt, updatedAt);
        this.client = client;
        this.employee = employee;
        this.service = service;
    }

    public getClient(): User {
        return this.client;
    }

    public setClient(client: User): void {
        this.client = client;
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
