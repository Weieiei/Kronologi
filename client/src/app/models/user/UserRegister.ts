export class UserRegister {

    public firstName: string;
    public lastName: string;
    public email: string;
    public username: string;
    public password: string;

    public constructor(firstName: string, lastName: string, email: string, username: string, password: string) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
    }

}
