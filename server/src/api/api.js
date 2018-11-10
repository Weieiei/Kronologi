const express = require('express');
const authenticate = require('./routes/authenticate');
const appointments = require('./routes/appointments');

const api = express.Router();

api.get('/', (req, res) => {
    res.send({ message: 'Hey world' });
});

api.use('/authenticate', authenticate);
api.use('/appointments', appointments);
module.exports = api;
