const express = require('express'),
    appointments = require('./routes/appointments');

const admin = express.Router();

admin.use('/appointments', appointments);

module.exports = admin;
