const express = require('express');
const knex = require('../../../../db/knex');

const appointments = express.Router();

appointments.get('/', (req, res) => {
    knex.select()
        .from('appointments')
        .then(appointments => {
            res.json(appointments);
        });
});

module.exports = appointments;
