import { EmployeeDTO } from '../user/employee-dto';

export interface EmployeeAppointmentDTO {
    id: number;
    date: string;
    startTime: string;
    endTime: string;
    employee: EmployeeDTO;
}
