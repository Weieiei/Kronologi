const express = require('express'),
    jwtWrapper = require('../../../../models/JWTWrapper'),
    knex = require('../../../../db/knex');

const appointments = express.Router();

appointments.get('/', jwtWrapper.verifyToken, (req, res) => {

    const {username} = req.body;
    knex.select().from('appointments')
        .then(appointments => {
            res.json(appointments);
        });

});

module.exports = appointments;
