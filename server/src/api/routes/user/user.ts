import express from 'express'

const userAppointments = require('./routes/appointments')
const user = express.Router();

user.use('/appointments', userAppointments);

module.exports = user;
