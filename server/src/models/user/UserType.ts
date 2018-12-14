/**
 * Using this as an enum for types of users (admin, client, employee).
 * It's better to use a string rather than an integer value.
 * It costs a bit more in terms of space, but saves some trouble in the long run,
 * like when we wanna conduct a query against the database but forget which number is associated to which type.
 */
export enum UserType {

    admin = 'admin',
    client = 'client',
    employee = 'employee'

}
