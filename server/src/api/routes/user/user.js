const express = require('express'),
    appointments = require('./routes/appointments');

const user = express.Router();

user.use('/appointments', appointments);

module.exports = user;
