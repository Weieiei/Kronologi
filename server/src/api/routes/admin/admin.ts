import express from 'express';

const admin = express.Router();
const appointments = require('./routes/appointments');
const employees = require('./routes/employees');

admin.use('/appointments', appointments);
admin.use('/employees', employees);

module.exports = admin;
