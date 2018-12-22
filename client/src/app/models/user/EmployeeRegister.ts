import { UserRegister } from './UserRegister';
import { EmployeeShiftTimes } from '../shift/EmployeeShiftTimes';

export class EmployeeRegister extends UserRegister {

    /**
     * List of id's of the services assigned to the employee.
     * List of shifts assigned to the employee.
     */
    private services: number[];
    private shifts: EmployeeShiftTimes[];

    public constructor(
        firstName: string, lastName: string,
        email: string, username: string, password: string,
        services: number[], shifts: EmployeeShiftTimes[]
    ) {
        super(firstName, lastName, email, username, password);
        this.services = services;
        this.shifts = shifts;
    }

    public getServices(): number[] {
        return this.services;
    }

    public setServices(services: number[]): void {
        this.services = services;
    }

    public getShifts(): EmployeeShiftTimes[] {
        return this.shifts;
    }

    public setShifts(shifts: EmployeeShiftTimes[]): void {
        this.shifts = shifts;
    }

}
