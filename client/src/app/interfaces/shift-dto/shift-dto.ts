import { EmployeeDTO } from '../employee/employee-dto';

export interface ShiftDTO {
    id: number;
    employee: EmployeeDTO;
    date: string;
    startTime: string;
    endTime: string;
}
