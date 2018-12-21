import { Appointment } from './Appointment';
import { User } from '../user/User';
import { Service } from '../service/Service';

export class AppointmentDetailed extends Appointment {

    private customer: User;
    private employee: User;
    private service: Service;

    public constructor(
        id: number, userId: number, employeeId: number, serviceId: number,
        startTime: Date, endTime: Date, notes: string,
        createdAt: Date, updatedAt: Date,
        customer: User, employee: User, service: Service
    ) {
        super(id, userId, employeeId, serviceId, startTime, endTime, notes, createdAt, updatedAt);
        this.customer = customer;
        this.employee = employee;
        this.service = service;
    }

    public getCustomer(): User {
        return this.customer;
    }

    public setCustomer(customer: User) {
        this.customer = customer;
    }

    public getEmployee(): User {
        return this.employee;
    }

    public setEmployee(employee: User) {
        this.employee = employee;
    }

    public getService(): Service {
        return this.service;
    }

    public setService(service: Service) {
        this.service = service;
    }

}
