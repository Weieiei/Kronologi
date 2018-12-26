export class EmployeeShift {

    private readonly id: number;
    private employeeId: number;
    private startTime: Date;
    private endTime: Date;
    private readonly createdAt: Date;
    private readonly updatedAt: Date;

    public constructor(
        id: number, employeeId: number,
        startTime: Date, endTime: Date,
        createdAt: Date, updatedAt: Date
    ) {
        this.id = id;
        this.employeeId = employeeId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public getId(): number {
        return this.id;
    }

    public getEmployeeId(): number {
        return this.employeeId;
    }

    public setEmployeeId(employeeId: number): void {
        this.employeeId = employeeId;
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

    public getCreatedAt(): Date {
        return this.createdAt;
    }

    public getUpdatedAt(): Date {
        return this.updatedAt;
    }

}
