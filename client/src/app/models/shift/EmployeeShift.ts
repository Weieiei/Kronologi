export class EmployeeShift {

    public readonly id: number;
    public employeeId: number;
    public startTime: Date;
    public endTime: Date;
    public readonly createdAt: Date;
    public readonly updatedAt: Date;

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

}
