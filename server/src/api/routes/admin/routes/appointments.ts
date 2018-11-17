import express from 'express'
import { Connection } from '../../../../db/knex'

/**
 * knex initialization -- we could create a global variable or create a TDG to have one instance of knex. As of right now
 * it is a bit all over the place.
 */
const knex = new Connection().knex()
const appointments = express.Router()

/**
 * @route       api/routes/admin/routes/appointment
 * @description Returns appointments to the admin ( returns all of them at the moment)
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
