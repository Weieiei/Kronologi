const express = require('express'),
    authenticate = require('./routes/authenticate'),
    services = require('./routes/services'),
    appointments = require('./routes/appointments/appointments');

const api = express.Router();

api.use('/authenticate', authenticate);
api.use('/services', services);
api.use('/appointments', appointments);

module.exports = api;
