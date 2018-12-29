import { UserRegister } from './UserRegister';
import { EmployeeShiftTimes } from '../shift/EmployeeShiftTimes';

export class EmployeeRegister extends UserRegister {

    /**
     * List of id's of the services assigned to the employee.
     * List of shifts assigned to the employee.
     */
    public services: number[];
    public shifts: EmployeeShiftTimes[];

    public constructor(
        firstName: string, lastName: string,
        email: string, username: string, password: string,
        services: number[], shifts: EmployeeShiftTimes[]
    ) {
        super(firstName, lastName, email, username, password);
        this.services = services;
        this.shifts = shifts;
    }

}
