const express = require('express');
const authenticate = require('./routes/authenticate');
const appointments = require('./routes/appointments');
services = require('./routes/services');

const api = express.Router();

api.get('/', (req, res) => {
    res.send({ message: 'Hey world' });
});

api.use('/authenticate', authenticate);
api.use('/admin', appointments);
api.use('/services', services);

module.exports = api;
