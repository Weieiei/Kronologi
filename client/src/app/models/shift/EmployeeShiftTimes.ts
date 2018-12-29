export class EmployeeShiftTimes {

    public number: number;
    public startTime: Date;
    public endTime: Date;

    public constructor(number: number, startTime: Date, endTime: Date) {
        this.number = number;
        this.startTime = startTime;
        this.endTime = endTime;
    }

}
