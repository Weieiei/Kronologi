const express = require('express'),
    admin = require('./admin/admin'),
    user = require('./user/user');

const appointments = express.Router();

appointments.use('/admin', admin);
appointments.use('/user', user);

module.exports = appointments;
