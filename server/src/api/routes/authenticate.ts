import { Connection } from '../../db/knex'
import { Client } from '../../models/user/Client'
import bcrypt from "bcrypt-nodejs";
import express from 'express'
const jwtWrapper = require('../../models/JWTWrapper');

let saltRounds = 10;
let authenticate = express.Router();
let passwordRegex = /^(?=.*\d)(?=.*[a-zA-Z]).{6,30}$/;

/**
 * @route       api/routes/authenticate/register
 * @description Register user
 * @access      Public
 */
authenticate.post('/register', (req, res) => {
    this.connector = new Connection().knex();

    //FIXME: Client should save its own instance into the db, we could relay this to the ./Models/Client ?
    const client : Client = new Client(req.body.user._firstName, req.body.user._lastName, req.body.user._email ,req.body.user._username,req.body.user._password)
    
    console.log(client)
    if (client.getPassword().length < 6 || client.getPassword().length > 30) {
        return res.status(400).send({ passwordError: 'Password must be between 6 and 30 characters.' });
    }
    else if (!passwordRegex.test(client.getPassword())) {
        return res.status(400).send({ passwordError: 'Password must contain at least 1 letter and 1 digit.' });
    }
    bcrypt.genSalt(saltRounds, (err, salt) => {
        bcrypt.hash(client.getPassword(), salt,null,(err, hash) => {

            if (err) {
                console.log(err);
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
                console.log(result[0])
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
                console.log(error)
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

/**
 * @route       api/routes/authenticate/login
 * @description Login User
 * @returns     JWT Token
 * @access      Public
 */
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
