import express from 'express'
import { Connection } from '../../../../db/knex'

const appointments = express.Router();

/**
 * Knex initialization
 */
const knex = new Connection().knex()

/**
 * @route       GET api/admin/appointments
 * @description Return list of all appointments to the admin.
 * @access      Private
 */
appointments.get('/', (req, res) => {

    knex.select('appointments.id', 'user_id', 'services.name', 'start_time', 'end_time', 'services.duration', 'notes',
                'users.first_name', 'users.last_name', 'users.email', 'users.username')
        .from('appointments')
        .innerJoin('users', 'appointments.user_id', 'users.id')
        .innerJoin('services', 'appointments.service_id', 'services.id')
        .then(appointments => {
            res.status(200).send({ appointments });
        });

});

module.exports = appointments;
