export interface BookAppointmentDTO {
    employeeId: number;
    serviceId: number;
    date: string;
    startTime: string;
    notes?: string;
}
