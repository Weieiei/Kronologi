import { EmployeeDTO } from '../employee/employee-dto';
import { AppointmentServiceDTO } from '../service/appointment-service-dto';

export interface UserAppointmentDTO {
    id: number;
    employee: EmployeeDTO;
    service: AppointmentServiceDTO;
    date: string;
    startTime: string;
    endTime: string;
    notes: string;
    status: string;
}
