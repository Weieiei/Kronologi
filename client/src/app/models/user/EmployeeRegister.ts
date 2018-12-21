import { UserRegister } from './UserRegister';

export class EmployeeRegister extends UserRegister {

    /**
     * List of id's of the services assigned to the employee.
     */
    private services: number[];

    public constructor(firstName: string, lastName: string, email: string, username: string, password: string, services: number[]) {
        super(firstName, lastName, email, username, password);
        this.services = services;
    }

    public getServices(): number[] {
        return this.services;
    }

    public setServices(services: number[]) {
        this.services = services;
    }

}
