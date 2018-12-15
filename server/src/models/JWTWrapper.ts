import * as jwt from 'jsonwebtoken';
const secretKey = 'secretKey';

export class JWTWrapper {

    static generateToken(payload): string {
        return jwt.sign(payload, secretKey);
    }

    static verifyToken(token): object | string {
        return jwt.verify(token, secretKey);
    }

}
