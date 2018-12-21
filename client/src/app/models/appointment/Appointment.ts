export class Appointment {

    private readonly id: number;
    private userId: number;
    private employeeId: number;
    private serviceId: number;
    private startTime: Date;
    private endTime: Date;
    private notes: string;
    private readonly createdAt: Date;
    private readonly updatedAt: Date;

    public constructor(
        id: number, userId: number, employeeId: number, serviceId: number,
        startTime: Date, endTime: Date, notes: string,
        createdAt: Date, updatedAt: Date
    ) {
        this.id = id;
        this.userId = userId;
        this.employeeId = employeeId;
        this.serviceId = serviceId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public getId(): number {
        return this.id;
    }

    public getUserId(): number {
        return this.userId;
    }

    public setUserId(userId: number): void {
        this.userId = userId;
    }

    public getEmployeeId(): number {
        return this.employeeId;
    }

    public setEmployeeId(employeeId: number): void {
        this.employeeId = employeeId;
    }

    public getServiceId(): number {
        return this.serviceId;
    }

    public setServiceId(serviceId: number): void {
        this.serviceId = serviceId;
    }

    public getStartTime(): Date {
        return this.startTime;
    }

    public setStartTime(startTime: Date): void {
        this.startTime = startTime;
    }

    public getEndTime(): Date {
        return this.endTime;
    }

    public setEndTime(endTime: Date): void {
        this.endTime = endTime;
    }

    public getNotes(): string {
        return this.notes;
    }

    public setNotes(notes: string): void {
        this.notes = notes;
    }

    public getCreatedAt(): Date {
        return this.createdAt;
    }

    public getUpdatedAt(): Date {
        return this.updatedAt;
    }

}
