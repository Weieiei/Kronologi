import express from 'express';
import { User } from '../../models/user/User';
import { UserType } from '../../models/user/UserType';
import * as bcrypt from 'bcrypt-nodejs';
import { Logger } from '../../models/logger';
import { EmailService } from '../../models/email/emailService';
import { validatePassword } from '../../helpers';
import { ValidationError } from 'objection';

const jwtWrapper = require('../../models/JWTWrapper');
// const logger = Logger.Instance.getGrayLog();

const saltRounds = 10;
const authenticate = express.Router();

const emailService = new EmailService();

const invalidCredentials = 'Incorrect username and/or password.';

/**
 * @route       POST api/authenticate/register
 * @description Register user of type client.
 * @access      Public
 */
authenticate.post('/register', async (req, res) => {

    const { firstName, lastName, email, username, password } = req.body;

    const pw = validatePassword(password);
    if (!pw['isValid']) return res.status(400).send({ error: pw['errors'] });

    bcrypt.genSalt(saltRounds, async (err, salt) => {

        bcrypt.hash(password, salt, undefined, async (err, hash) => {

            if (err) {
                console.log(err);
                // logger.error('error with bcrypt', { error: err });
                return res.status(500).send({ error: 'Something went wrong with bcrypt.' });
            }

            try {

                const user = await User
                    .query()
                    .insert({ firstName, lastName, email, username, password: hash, userType: UserType.client });

                const token: string = generateToken(user.id, user.userType);
                emailService.sendEmail(email, 'Registration Successful', 'Congratulations!!');
                return res.status(200).send({ token });

            }
            catch (error) {
                if (error instanceof ValidationError) {
                    const message: string = error.message;
                    // logger.error('client registration failed', { error } );
                    return res.status(400).send({ message });
                }
                else {
                    // logger.error('client registration failed', { error } );
                    return res.status(500).send({ error });
                }
            }

        });

    });

});

/**
 * @route       POST api/authenticate/login
 * @description Login user of any type.
 * @access      Public
 */
authenticate.post('/login', async (req, res) => {

    const { username, password } = req.body;

    const user = await User.query().where({ username }).first();

    if (!user) return res.status(401).send({ invalidCredentials });

    bcrypt.compare(password, user.password, (err, match) => {

        if (err) {
            console.log(err);
            // logger.error('error with bcrypt', { errorData: err });
            return res.status(500).send({ error: 'Something went wrong with bcrypt.' });
        }

        if (match) {
            const token: string = generateToken(user.id, user.userType);
            return res.status(200).send({ token });
        }
        else {
            return res.status(401).send({ invalidCredentials });
        }

    });

});

function generateToken(userId: number, userType: string): string {
    const payload: string | Buffer | object = { subject: userId, type: userType };
    return jwtWrapper.generateToken(payload);
}

module.exports = authenticate;
