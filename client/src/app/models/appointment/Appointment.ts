export class Appointment {

    public readonly id: number;
    public clientId: number;
    public employeeId: number;
    public serviceId: number;
    public date: Date;
    public startTime: Date;
    public endTime: Date;
    public notes: string;
    public status: string;
    public readonly createdAt: Date;
    public readonly updatedAt: Date;

    public constructor(
        id: number, clientId: number, employeeId: number, serviceId: number,
        date: Date, startTime: Date, endTime: Date, notes: string, status: string,
        createdAt: Date, updatedAt: Date
    ) {
        this.id = id;
        this.clientId = clientId;
        this.employeeId = employeeId;
        this.serviceId = serviceId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.notes = notes;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
