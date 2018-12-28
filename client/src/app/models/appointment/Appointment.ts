export class Appointment {

    public readonly id: number;
    public userId: number;
    public employeeId: number;
    public serviceId: number;
    public startTime: Date;
    public endTime: Date;
    public notes: string;
    public status: string;
    public readonly createdAt: Date;
    public readonly updatedAt: Date;

    public constructor(
        id: number, userId: number, employeeId: number, serviceId: number,
        startTime: Date, endTime: Date, notes: string, status: string,
        createdAt: Date, updatedAt: Date
    ) {
        this.id = id;
        this.userId = userId;
        this.employeeId = employeeId;
        this.serviceId = serviceId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.notes = notes;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
