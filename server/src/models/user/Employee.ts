import { User } from './User';

export class Employee extends User {

    constructor(fname: string, lname: string, email: string, username: string, password: string) {
        super(fname, lname, email, username, password);
    }

    public getType(): string {
        return Employee.name;
    }

}
