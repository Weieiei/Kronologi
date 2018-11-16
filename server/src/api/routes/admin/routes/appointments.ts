import express from 'express'
import { Connection } from '../../../../db/knex'

/**
 * knex initialization -- we could create a global variable or create a TDG to have one instance of knex. As of right now
 * it is a bit all over the place.
 */
const knex = new Connection().knex()
const appointments = express.Router()

appointments.get('/', (req, res) => {

    const {username} = req.body;
    knex.select().from('appointments')
        .then(appointments => {
            res.json(appointments);
        });

});

module.exports = appointments;
