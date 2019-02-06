import { EmployeeDTO } from '../employee/employee-dto';

export interface ServiceDTO {
    id: number;
    name: string;
    duration: number;
    employees: EmployeeDTO[];
}
