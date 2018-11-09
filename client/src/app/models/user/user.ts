export class User {

    private _id: number;
    private _firstName: string;
    private _lastName: string;
    private _email: string;
    private _username: string;
    private _password: string;
    private _userType: number;

    constructor(
        id: number = null,
        firstName: string = '', lastName: string = '',
        email: string = '', username: string = '', password: string = '',
        userType: number = null
    ) {
        this._id = id;
        this._firstName = firstName;
        this._lastName = lastName;
        this._email = email;
        this._username = username;
        this._password = password;
        this._userType = userType
    }

    public get id(): number {
        return this._id;
    }

    public get firstName(): string {
        return this._firstName;
    }

    public set firstName(firstName: string) {
        this._firstName = firstName;
    }

    public get lastName(): string {
        return this._lastName;
    }

    public set lastName(lastName: string) {
        this._lastName = lastName;
    }

    public get email(): string {
        return this._email;
    }

    public set email(email: string) {
        this._email = email;
    }

    public get username(): string {
        return this._username;
    }

    public set username(username: string) {
        this._username = username;
    }

    public get password(): string {
        return this._password;
    }

    public set password(password: string) {
        this._password = password;
    }

    public get userType(): number {
        return this._userType;
    }

    public set userType(userType: number) {
        this._userType = userType;
    }

}
