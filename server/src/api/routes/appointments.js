const express = require('express'),
knex = require('../../db/knex');

const appointments = express.Router();

appointments.get('/appointments', (req, res) => {
    const {username} = req.body;
     knex.select().from('appointments')
      .then(appointments => {
        res.json(appointments);
        console.log(appointments);
        console.log(appointments[0]);
      });
});
 module.exports = appointments;