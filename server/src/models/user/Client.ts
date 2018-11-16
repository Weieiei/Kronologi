import { User } from "./User";

export class Client extends User{            

    //FIXME: keep phone number later on to have text reminders
    private _phone: string;
    private _username: string;

    constructor( fname: string, lname: string, email: string, username : string,  password: string) {
        super( fname, lname, email, password);
        this._username = username;
    }


    public setUsername(username:string): void{
        this._username = username;
    }
    public getUsername():string{
        return this._username;
    }

    public getType(): string {
        return Client.name;
    }
}