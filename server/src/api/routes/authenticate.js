const express = require('express'),
    bcrypt = require('bcryptjs'),
    knex = require('../../db/knex'),
    jwtWrapper = require('../../models/JWTWrapper'),
    USER_TYPE = require('../../models/user/USER_TYPE');

const authenticate = express.Router();

const saltRounds = 10;
const passwordRegex = /^(?=.*\d)(?=.*[a-zA-Z]).{6,30}$/;

authenticate.post('/register', (req, res) => {

    const { _firstName, _lastName, _email, _username, _password } = req.body.user;

    if (_password.length < 6 || _password.length > 30) {
        return res.status(400).send({ passwordError: 'Password must be between 6 and 30 characters.' });
    }
    else if (!passwordRegex.test(_password)) {
        return res.status(400).send({ passwordError: 'Password must contain at least 1 letter and 1 digit.' });
    }

    bcrypt.genSalt(saltRounds, (err, salt) => {
        bcrypt.hash(_password, salt, (err, hash) => {

            if (err) {
                console.log(err);
                return res.status(500).send({ error: 'Something went wrong with bcrypt.' });
            }

            knex('users').insert({
                first_name: _firstName,
                last_name: _lastName,
                email: _email,
                username: _username,
                password: hash,
                user_type: USER_TYPE.CLIENT
            })
            .returning('id')
            .then(result => {

                const token = generateToken(result[0]);
                return res.status(200).send({ token });

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

                return res.status(500).send({ error });

            })

        })
    })
});

authenticate.post('/login', (req, res) => {

    const { username, password } = req.body;

    knex.select().from('users').where('username', username)
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
    })

});

function generateToken(user_id) {
    const payload = { subject: user_id };
    return jwtWrapper.generateToken(payload);
}

module.exports = authenticate;
