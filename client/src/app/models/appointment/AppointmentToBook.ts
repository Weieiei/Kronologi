export class AppointmentToBook {

    public employeeId: number;
    public serviceId: number;
    public startTime: Date;
    public notes: string;

    public constructor(employeeId: number, serviceId: number, startTime: Date, notes: string) {
        this.employeeId = employeeId;
        this.serviceId = serviceId;
        this.startTime = startTime;
        this.notes = notes;
    }

}
