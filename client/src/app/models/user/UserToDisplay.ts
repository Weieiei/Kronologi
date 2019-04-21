export class UserToDisplay {

    public readonly id: number;
    public firstName: string;
    public lastName: string;
    public email: string;
    public userRole: string;
    public services: any[];
    public readonly createdAt: Date;
    public readonly updatedAt: Date;

    public constructor(

        id: number, firstName: string, lastName: string,
        email: string, userRole: string, services: any[],
        createdAt: Date, updatedAt: Date
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userRole = userRole;
        this.services = services;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
