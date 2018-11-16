
import { User } from "./User";

export class Admin extends User {
    constructor(fname: string, lname: string, email: string, password: string) {
        super(fname, lname, email, password);
    }

    public getType(): string {
        return Admin.name;
    }

}