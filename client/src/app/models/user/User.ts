export class User {

    public readonly id: number;
    public firstName: string;
    public lastName: string;
    public email: string;
    public username: string;
    public password: string;
    public userType: string;
    public readonly createdAt: Date;
    public readonly updatedAt: Date;

    public constructor(
        id: number, firstName: string, lastName: string,
        email: string, username: string, password: string,
        userType: string, createdAt: Date, updatedAt: Date
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
