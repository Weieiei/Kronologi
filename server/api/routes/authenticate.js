const express = require('express'),
    bcrypt = require('bcryptjs'),
    jwt = require('jsonwebtoken'),
    db = require('../../db');

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

            db.query(`
            INSERT INTO users
            (first_name, last_name, email, username, password) VALUES
            ($1, $2, $3, $4, $5)
            RETURNING user_id;
            `, [first_name, last_name, email, username, hash],
            (error, result) => {

                if (error) {
                    switch (error.constraint) {
                        case 'users_email_key':
                            return res.status(400).send({ emailError: 'An account with this email already exists.' });
                        case 'users_username_key':
                            return res.status(400).send({ usernameError: 'This username is taken.' });
                    }
                    return res.status(500).send({ error: 'Something went wrong.' });
                }
                else {
                    const user_id = result.rows[0].user_id;
                    const token = generateToken(user_id);
                    const message = 'User successfully created.';

                    return res.status(200).send({
                        user_id,
                        token,
                        message
                    });
                }

            })

        })
    })
})

authenticate.post('/login', (req, res) => {
    const { username, password } = req.body;

    db.query(`
    SELECT user_id, username, password FROM users
    WHERE username = $1;
    `, [username], (error, result) => {

        if (error) {
            console.log(error);
            return res.status(500).send({ error: 'Something went wrong.' });
        }

        const invalidCredentials = 'Incorrect username and/or password.';

        if (!result.rows.length) {
            return res.status(401).send({ invalidCredentials });
        }

        bcrypt.compare(password, result.rows[0].password, (err, match) => {

            if (err) {
                console.log(err);
                return res.status(500).send({ error: 'Something went wrong with bcrypt.' });
            }

            if (match) {
                const user_id = result.rows[0].user_id;
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
})

function generateToken(user_id) {
    const payload = { subject: user_id };
    const token = jwt.sign(payload, 'secretKey');
    return token;
}

module.exports = authenticate;
