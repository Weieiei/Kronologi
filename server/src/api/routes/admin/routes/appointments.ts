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
        .eager('[user, service]');

    res.status(200).send({ appointments });

});

module.exports = appointments;
