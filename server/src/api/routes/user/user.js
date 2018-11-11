const express = require('express');

const appointments = require('./routes/appointment');

const user = express.Router();

user.use('/appointments', appointments);

module.exports = user;
