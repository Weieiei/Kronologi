import express from 'express';
import { bookAppointment, cancelAppointment, getMyAppointments } from '../../../controllers/appointment';

export const user = express.Router();

/**
 * @route       GET api/user/appointments
 * @description Get all of your appointments that are either today or in the future.
 * @access      Private
 */
user.get('/appointments', getMyAppointments);

/**
 * @route       POST api/user/appointments
 * @description Book an appointment.
 * @access      Private
 */
user.post('/appointments', bookAppointment);

/**
 * @route       POST api/user/appointments/:id/cancel
 * @description Cancel an appointment.
 * @param       id: id of the appointment.
 * @access      Private
 */
user.post('/appointments/:id/cancel', cancelAppointment);
