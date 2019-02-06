import { EmployeeDTO } from '../user/employee-dto';

export interface EmployeeAppointmentDTO {
    id: number;
    employee: EmployeeDTO;
    date: string;
    startTime: string;
    endTime: string;
}
