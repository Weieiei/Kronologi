import { EmployeeDTO } from '../user/employee-dto';

export interface ShiftDTO {
    id: number;
    employee: EmployeeDTO;
    date: string;
    startTime: string;
    endTime: string;
}
