import { AdminEmployeeDTO } from '../employee/admin-employee-dto';

export interface AdminEmployeeShiftDTO {
    id: number;
    employee: AdminEmployeeDTO;
    date: string;
    startTime: string;
    endTime: string;
}
