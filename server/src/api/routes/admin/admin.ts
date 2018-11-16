import express from 'express'

const adminRoute = express.Router();
const adminAppointments = require('./routes/appointments')

adminRoute.use('/appointments', adminAppointments);

module.exports = adminRoute;
