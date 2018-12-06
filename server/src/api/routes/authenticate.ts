import express from 'express';
import { Connection } from '../../db/knex';
import { Client } from '../../models/user/Client';
import bcrypt from "bcrypt-nodejs";
import { Logger } from '../../models/logger';
import { EmailService} from '../../models/email/emailService';

const jwtWrapper = require('../../models/JWTWrapper');
const logger = Logger.Instance.getGrayLog();

let saltRounds = 10;
let authenticate = express.Router();
let passwordRegex = /^(?=.*\d)(?=.*[a-zA-Z]).{6,30}$/;

/**
 * @route       POST api/authenticate/register
 * @description Register user.
 * @access      Public
 */
authenticate.post('/register', (req, res) => {

    this.connector = new Connection().knex();
    let email = new EmailService();
    const { _firstName, _lastName, _email, _username, _password } = req.body.user;
    const client: Client = new Client(_firstName, _lastName, _email, _username, _password);

    if (client.getPassword().length < 6 || client.getPassword().length > 30) {
        return res.status(400).send({ passwordError: 'Password must be between 6 and 30 characters.' });
    }
    else if (!passwordRegex.test(client.getPassword())) {
        return res.status(400).send({ passwordError: 'Password must contain at least 1 letter and 1 digit.' });
    }

    bcrypt.genSalt(saltRounds, (err, salt) => {
        
        bcrypt.hash(client.getPassword(), salt, null, (err, hash) => {

            if (err) {
                console.log(err);
                logger.error('error with bcrypt', { errorData: err });
                return res.status(500).send({ error: 'Something went wrong with bcrypt.' });
            }

            client.setPassword(hash)

            this.connector.table('users').insert({
                first_name: client.getFirstName(),
                last_name:  client.getLastName(),
                email: client.getEmail(),
                username: client.getUsername(),
                password: client.getPassword(),
                user_type: client.getType()
            })
            .returning('id')
            .then(result => {
                email.sendEmail(client.getEmail(), "Registration Successful", "Congratulations!!");
                const token: string = generateToken(result[0]);
                return res.status(200).send({ token });

            })
            .catch(error => {

                switch (error.constraint) {
                    case 'users_first_name_length':
                    case 'users_last_name_length':
                        return res.status(400).send({ nameError: 'Both first and last names should be at least 2 characters long.' });
                    case 'users_email_unique':
                        return res.status(400).send({ emailError: 'An account with this email already exists.' });
                    case 'users_username_unique':
                        return res.status(400).send({ usernameError: 'This username is taken.' });
                    case 'users_username_length':
                        return res.status(400).send({ usernameError: 'Usernames should be between 4 and 30 characters.' });
                }

                logger.error('client registration failed', { errorData: error }, { clientId: client.getId() });
                return res.status(500).send({ error });

            });

        });

    });

});

/**
 * @route       POST api/authenticate/login
 * @description Login user.
 * @access      Public
 */
authenticate.post('/login', (req, res) => {

    this.connector = new Connection().knex();
    debugger
    const { username, password } = req.body;
    
    this.connector.select().from('users').where('username', username)
    .then(user => {

        const invalidCredentials = 'Incorrect username and/or password.';

        if (!user.length) {
            return res.status(401).send({ invalidCredentials });
        }

        bcrypt.compare(password, user[0].password, (err, match) => {

            if (err) {
                console.log(err);
                logger.error('error with bcrypt', { errorData: err });
                return res.status(500).send({ error: 'Something went wrong with bcrypt.' });
            }

            if (match) {
                const token = generateToken(user[0].id);
                return res.status(200).send({ token });
            }
            else {
                return res.status(401).send({ invalidCredentials });
            }

        });
        
    })
    .catch(error => {
        logger.error('error with login', { errorData: error });
        return res.status(500).send({ error });
    });

});

function generateToken(user_id: number): string {
    const payload: string | Buffer | object = { subject: user_id };
    return jwtWrapper.generateToken(payload);
}

module.exports = authenticate;
