import { generateToken, hashPassword, passwordsMatch, validatePassword } from '../helpers/helper_functions';
import { User } from '../models/user/User';
import { UserType } from '../models/user/UserType';
import { ValidationError } from 'objection';
import { Logger } from '../models/logger';
import { EmailService } from '../models/email/emailService';

const logger = Logger.Instance.getGrayLog();
const emailService = new EmailService();
const invalidCredentials = 'Incorrect username and/or password.';

export const register = async (req, res) => {

    const { firstName, lastName, email, username, password } = req.body.user;

    try {
        validatePassword(password);
    }
    catch (error) {
        return res.status(400).send({ message: error.message });
    }

    try {

        const user = await User
            .query()
            .insert({ firstName, lastName, email, username, password: await hashPassword(password), userType: UserType.client });

        const token: string = generateToken(user.id, user.userType);
        emailService.sendEmail(email, 'Registration Successful', 'Congratulations!!');
        return res.status(200).send({ token });

    }
    catch (error) {

        logger.error('client registration failed', { error } );

        if (error instanceof ValidationError) {
            return res.status(400).send({ message: error.message });
        }
        else {
            return res.status(500).send({ error });
        }

    }

};

export const login = async (req, res) => {

    const { username, password } = req.body.user;

    const user = await User.query().where({ username }).first();
    if (!user) return res.status(401).send({ invalidCredentials });

    if (await passwordsMatch(password, user.password)) {
        const token: string = generateToken(user.id, user.userType);
        return res.status(200).send({ token });
    }
    else {
        return res.status(401).send({ invalidCredentials });
    }

};
