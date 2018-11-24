import express from 'express';

const user = express.Router();
const appointments = require('./routes/appointments');

user.use('/appointments', appointments);

module.exports = user;
