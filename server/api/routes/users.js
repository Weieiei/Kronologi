const express = require('express'),
    users = express.Router();

users.get('/v1', (req, res) => {
    res.send('hello');
})

module.exports = users;
