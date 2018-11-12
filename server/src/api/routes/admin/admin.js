const express = require('express');
const appointments = require('./routes/appointments');

const admin = express.Router();

admin.use('/appointments', appointments);

module.exports = admin;
