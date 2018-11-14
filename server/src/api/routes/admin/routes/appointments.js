const express = require('express');
const knex = require('../../../../db/knex');

const appointments = express.Router();

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
