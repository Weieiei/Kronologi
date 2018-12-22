import { UserRegister } from './UserRegister';

export class EmployeeRegister extends UserRegister {

    /**
     * List of id's of the services assigned to the employee.
     */
    private employeeServices: number[];

    public constructor(firstName: string, lastName: string, email: string, username: string, password: string, employeeServices: number[]) {
        super(firstName, lastName, email, username, password);
        this.employeeServices = employeeServices;
    }

    public getEmployeeServices(): number[] {
        return this.employeeServices;
    }

    public setEmployeeServices(employeeServices: number[]) {
        this.employeeServices = employeeServices;
    }

}
