import express from 'express'

const adminRoute = express.Router();
const adminAppointments = require('./routes/appointments')

/**
 * @route       api/routes/admin/appointments
 * @description return all appointments to the administrator
 * @access      Private
 */
adminRoute.use('/appointments', adminAppointments);

module.exports = adminRoute;
