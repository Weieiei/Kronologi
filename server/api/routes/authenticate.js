const express = require('express'),
    db = require('../../db'),
    authenticate = express.Router();

authenticate.post('/register', (req, res) => {
    const { first_name, last_name, email, username, password } = req.body.user;

    db.query(`
    INSERT INTO users
    (first_name, last_name, email, username, password_hash) VALUES
    ($1, $2, $3, $4, $5)
    RETURNING user_id;
    `, [first_name, last_name, email, username, password],
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
            return res.status(200).send({ user_id });
        }

    })
})

module.exports = authenticate;
