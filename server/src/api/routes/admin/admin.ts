import express from 'express';

const admin = express.Router();
const appointments = require('./routes/appointments');

admin.use('/appointments', appointments);

module.exports = admin;
