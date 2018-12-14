import express from 'express';
import { Appointment } from '../../../../models/appointment/Appointment';

const appointments = express.Router();

/**
 * @route       GET api/admin/appointments
 * @description Return list of all appointments to the admin.
 * @access      Private
 */
appointments.get('/', async (req, res) => {

    const appointments = await Appointment
        .query()
        .select(
            'appointments.*',
            'users.first_name as first_name', 'users.last_name as last_name', 'users.email as email', 'users.username as username',
            'services.name as name', 'services.duration as duration'
        )
        .join('users', 'appointments.user_id', 'users.id')
        .join('services', 'appointments.service_id', 'services.id');

    res.status(200).send({ appointments });

});

module.exports = appointments;
