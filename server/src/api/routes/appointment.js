const express = require('express'),
    knex = require('../../db/knex');

const services = express.Router();

appointments.get('', (req, res) => {

    knex.select().from('appointments')
    .then(appointments => {
    res.json(appointments);
});
});

appointments.post( => {
    knex('appointments').insert({
    id:_id,
    user_id: _user_id,
    service_id:_service_id,
    date: _date,
    start_time: _lastName,
    end_time: _email,
    notes: _notes
})
});

module.exports = appointments;