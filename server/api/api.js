const express = require('express'),
    api = express.Router(),
    authenticate = require('./routes/authenticate');

api.get('/', (req, res) => {
    res.send({ message: 'Hey world' });
})

api.use('/authenticate', authenticate);

module.exports = api;
