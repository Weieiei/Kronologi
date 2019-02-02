import { Time } from '@angular/common';

export class Appointment {

    public readonly id: number;
    public clientId: number;
    public employeeId: number;
    public serviceId: number;
    public startTime: Time;
    public endTime: Time;
    public date: Date;
    public notes: string;
    public status: string;
    public readonly createdAt: Date;
    public readonly updatedAt: Date;

    public constructor(
        id: number, clientId: number, employeeId: number, serviceId: number,
        startTime: Time, endTime: Time, date: Date, notes: string, status: string,
        createdAt: Date, updatedAt: Date
    ) {
        this.id = id;
        this.clientId = clientId;
        this.employeeId = employeeId;
        this.serviceId = serviceId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.notes = notes;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
