const express = require('express'),
    authenticate = require('./routes/authenticate');

const api = express.Router();

api.get('/', (req, res) => {
    res.send({ message: 'Hey world' });
});

api.use('/authenticate', authenticate);

module.exports = api;
