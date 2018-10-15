const express = require('express'),
    api = express.Router(),
    users = require('./routes/users');

api.get('/', (req, res) => {
    res.send({ message: 'Hey world' });
})

api.use('/users', users);

module.exports = api;
