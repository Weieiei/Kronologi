const express = require('express'),
    bcrypt = require('bcryptjs'),
    jwt = require('jsonwebtoken'),
    knex = require('../../db/knex');

const authenticate = express.Router();

const saltRounds = 10;
const passwordRegex = /^(?=.*\d)(?=.*[a-zA-Z]).{6,30}$/;

authenticate.post('/register', (req, res) => {

    const { first_name, last_name, email, username, password } = req.body.user;

    if (password.length < 6 || password.length > 30) {
        return res.status(400).send({ passwordError: 'Password must be between 6 and 30 characters.' });
    }
    else if (!passwordRegex.test(password)) {
        return res.status(400).send({ passwordError: 'Password must contain at least 1 letter and 1 digit.' });
    }

    bcrypt.genSalt(saltRounds, (err, salt) => {
        bcrypt.hash(password, salt, (err, hash) => {

            if (err) {
                console.log(err);
                return res.status(500).send({ error: 'Something went wrong with bcrypt.' });
            }

            knex('users').insert({
                first_name,
                last_name,
                email,
                username,
                password: hash
            })
            .returning('user_id')
            .then(result => {

                const user_id = result[0];
                const token = generateToken(user_id);
                const message = 'User successfully created.';

                return res.status(200).send({
                    user_id,
                    token,
                    message
                });

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
})

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
                const user_id = user[0].user_id;
                const token = generateToken(user_id);
                const message = 'User successfully logged in.';

                return res.status(200).send({
                    user_id,
                    token,
                    message
                });
            }
            else {
                return res.status(401).send({ invalidCredentials });
            }

        })

    })
    .catch(error => {

        return res.status(500).send({ error });

    })

})

function generateToken(user_id) {
    const payload = { subject: user_id };
    const token = jwt.sign(payload, 'secretKey');
    return token;
}

module.exports = authenticate;
