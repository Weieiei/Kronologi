import { User } from "./User";

export class Client extends User{            

    //todo keep phone number later on to have text reminders
    private phone: string;
    private username: string;

    constructor( fname: string, lname: string, email: string, username : string,  password: string) {
        super( fname, lname, email, password);
        this.username = username;
    }


    public setUsername(username:string): void{
        this.username = username;
    }
    public getUsername():string{
        return this.username;
    }

    public getType(): string {
        return Client.name;
    }
}