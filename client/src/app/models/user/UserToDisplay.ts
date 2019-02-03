import { Service } from "../service/Service";

export class UserToDisplay {

    public readonly id: number;
    public firstName: string;
    public lastName: string;
    public email: string;
    public userRoles: string[];
    public services: string[];
    public readonly createdAt: Date;
    public readonly updatedAt: Date;

    public constructor(
        id: number, firstName: string, lastName: string,
        email: string, userRoles: string[], services: string[],
        createdAt: Date, updatedAt: Date
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userRoles = userRoles;
        this.services = services;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
