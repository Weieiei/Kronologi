import { EmployeeShiftTimes } from '../models/shift/EmployeeShiftTimes';
import { UserRegisterDTO } from './user-register-dto';

export interface EmployeeRegisterDTO extends UserRegisterDTO {
    /**
     * List of id's of the services assigned to the employee.
     * List of shifts assigned to the employee.
     */
    services: number[];
    shifts: EmployeeShiftTimes[];
}
