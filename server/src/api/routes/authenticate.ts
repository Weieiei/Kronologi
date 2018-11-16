import { Connection } from '../../db/knex'
import { Client } from '../../models/user/Client'
import bcrypt from "bcrypt-nodejs";
import express from 'express'
const jwtWrapper = require('../../models/JWTWrapper');

let saltRounds = 10;
let authenticate = express.Router();
let passwordRegex = /^(?=.*\d)(?=.*[a-zA-Z]).{6,30}$/;

/**
 * A client object was created here. However, in my opinion, it would be optimal to have a save() function returning 
 * a promise in the Client class that handles all that to be sure we follow encapsulation principles as well.
 */
authenticate.post('/register', (req, res) => {
    this.connector = new Connection().knex();
    console.log(req.body)
    const client : Client = new Client(req.body.user._firstName, req.body.user._lastName, req.body.user._email ,req.body.user._username,req.body._password)
    console.log(client.getPassword())
    if (client.getPassword().length < 6 || client.getPassword().length > 30) {
        return res.status(400).send({ passwordError: 'Password must be between 6 and 30 characters.' });
    }
    else if (!passwordRegex.test(client.getPassword())) {
        return res.status(400).send({ passwordError: 'Password must contain at least 1 letter and 1 digit.' });
    }
    bcrypt.genSalt(saltRounds, (err, salt) => {
        bcrypt.hash(client.getPassword(), salt, (err, hash) => {

            if (err) {
                console.log(err);
                return res.status(500).send({ error: 'Something went wrong with bcrypt.' });
            }
            client.setPassword(hash)
            this.connector.table('users').insert([
                client.getFName(),
                client.getLName(),
                client.getEmail(),
                client.getUsername(),
                client.getPassword(),
            ])
            .returning('id')
            .then(result => {

                client.setId(result[0]);
                client.setToken(generateToken(client.getId()));
                const message = 'User successfully created.';

                return res.status(200).send([
                    client.getId(),
                    client.getToken(),
                    message
                ]);
            })
                .catch(error => {
                switch (error.constraint) {
                    case 'users_first_name_length':
                    case 'users_last_name_length':
                        return res.status(400).send({ nameError: 'Both first and last names should be at least 2 characters long each.' });
                    case 'users_email_unique':
                        return res.status(400).send({ emailError: 'An account with this email already exists.' });
                    case 'users_username_unique':
                        return res.status(400).send({ usernameError: 'This username is taken.' });
                    case 'users_username_length':
                        return res.status(400).send({ usernameError: 'Usernames should be between 4 and 30 characters.' });
                }
                console.log(error)
                return res.status(500).send({ error });
            });
        });
    });
});
authenticate.post('/login', (req, res) => {

    this.connector = new Connection().knex();

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
        return res.status(500).send({ error });
    });
});
function generateToken(user_id) {
    const payload = { subject: user_id };
    return jwtWrapper.generateToken(payload);
}
module.exports = authenticate;
//# sourceMappingURL=authenticate.js.map
