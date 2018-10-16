const express = require('express'),
    bcrypt = require('bcrypt'),
    db = require('../../db'),
    saltRounds = 10,
    passwordRegex = /^(?=.*\d)(?=.*[a-zA-Z]).{6,30}$/,
    authenticate = express.Router();

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
                    const message = 'User successfully created.';
                    return res.status(200).send({
                        user_id,
                        message
                    });
                }

            })

        })
    })
})

module.exports = authenticate;
