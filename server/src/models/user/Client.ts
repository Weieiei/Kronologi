import { User } from "./User";

export class Client extends User {            

    // TODO: phone numbers for clients (SMS reminders)
    private _phone: string;

    constructor(fname: string, lname: string, email: string, username : string,  password: string) {
        super(fname, lname, email, username, password);
    }

    public getType(): string {
        return Client.name;
    }

}
