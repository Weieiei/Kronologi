const express = require('express'),
    knex = require('../../../../db/knex');

const appointments = express.Router();

appointments.get('/', (req, res) => {

    const {username} = req.body;
    knex.select().from('appointments')
        .then(appointments => {
            res.json(appointments);
        });

});

module.exports = appointments;
