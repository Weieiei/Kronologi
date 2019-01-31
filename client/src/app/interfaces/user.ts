
export interface User {
    readonly id: number;
    firstName: string;
    lastName: string;
    email: string;
    username: string;
    password: string;
    userType: string;
    readonly createdAt: Date;
    readonly updatedAt: Date;
}
