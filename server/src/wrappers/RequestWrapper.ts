import { Request } from 'express';

/**
 * When compiling the TypeScript code, we get an error saying the userId/userType properties don't exist in the Request type.
 * So we created this interface that extends from it, and allows those properties to be assigned values.
 * Source: https://stackoverflow.com/questions/44383387/typescript-error-property-user-does-not-exist-on-type-request/44386122#44386122
 */
export interface RequestWrapper extends Request {

    userId: number;
    userType: string;

}
