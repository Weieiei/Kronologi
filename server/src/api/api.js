const express = require('express'),
    authenticate = require('./routes/authenticate'),
    services = require('./routes/services');

const api = express.Router();

api.get('/', (req, res) => {
    res.send({ message: 'Hey world' });
});

api.use('/authenticate', authenticate);
api.use('/services', services);

module.exports = api;
