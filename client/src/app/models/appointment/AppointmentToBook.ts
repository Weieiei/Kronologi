export class AppointmentToBook {

    private employeeId: number;
    private serviceId: number;
    private startTime: Date;
    private notes: string;

    public constructor(employeeId: number, serviceId: number, startTime: Date, notes: string) {
        this.employeeId = employeeId;
        this.serviceId = serviceId;
        this.startTime = startTime;
        this.notes = notes;
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

    public getNotes(): string {
        return this.notes;
    }

    public setNotes(notes: string): void {
        this.notes = notes;
    }

}
