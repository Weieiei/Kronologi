import { EmployeeDTO } from '../user/employee-dto';

export interface ServiceDTO {
    id: number;
    name: string;
    duration: number;
    employees: EmployeeDTO[];
}
