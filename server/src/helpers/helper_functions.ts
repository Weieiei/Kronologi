import * as bcrypt from 'bcrypt-nodejs';
import { JWTWrapper } from '../wrappers/JWTWrapper';

const passwordRegex = /^(?=.*\d)(?=.*[a-zA-Z]).{6,30}$/;
const saltRounds: number = 10;

export function validatePassword(password: string): void {

    if (password.length < 6 || password.length > 30) {
        throw Error('Password must be between 6 and 30 characters.');
    }
    else if (!passwordRegex.test(password)) {
        throw Error('Password must contain at least 1 letter and 1 digit.');
    }

}

export function hashPassword(password: string): Promise<string> {

    return new Promise<string>((resolve, reject) => {

        bcrypt.genSalt(saltRounds, (err, salt: string) => {

            bcrypt.hash(password, salt, undefined, (err, hash: string) => {

                if (err) reject(err);
                resolve(hash);

            });

        });

    });

}

export function passwordsMatch(attemptedPassword, correctPassword): Promise<boolean> {

    return new Promise<boolean>((resolve, reject) => {

        bcrypt.compare(attemptedPassword, correctPassword, (err, match: boolean) => {

            if (err) reject (err);
            resolve(match);

        });

    });

}

export function generateToken(userId: number, userType: string): string {
    const payload: string | Buffer | object = { subject: userId, type: userType };
    return JWTWrapper.generateToken(payload);
}
