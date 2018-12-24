export class EmployeeShiftTimes {

    private number: number;
    private startTime: Date;
    private endTime: Date;

    public constructor(number: number, startTime: Date, endTime: Date) {
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public getNumber(): number {
        return this.number;
    }

    public setNumber(number: number): void {
        this.number = number;
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

}
