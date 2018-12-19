import express from 'express';
import { getAllAppointments } from '../../../controllers/appointment';
import { createEmployee } from '../../../controllers/exployee';

export const admin = express.Router();

/**
 * @route       GET api/admin/appointments
 * @description Return list of all appointments to the admin.
 * @access      Private
 */
admin.get('/appointments', getAllAppointments);

/**
 * @route       POST api/admin/employees/register
 * @description Create a user of type employee.
 * @access      Private
 */
admin.post('/employees/register', createEmployee);
