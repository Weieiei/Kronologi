import { User } from './User';

export class Admin extends User {

    constructor(fname: string, lname: string, email: string, username: string, password: string) {
        super(fname, lname, email, username, password);
    }

}
