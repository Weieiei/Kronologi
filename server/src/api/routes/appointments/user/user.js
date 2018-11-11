const express = require('express'),
    jwtWrapper = require('../../../../models/JWTWrapper'),
    knex = require('../../../../db/knex');

const userAppts = express.Router();

userAppts.get('/', jwtWrapper.verifyToken, (req, res) => {

    const userId = req.userId;

    knex.select().from('appointments')
        .innerJoin('services', 'appointments.service_id', 'services.id')
        .where('appointments.user_id', userId)
    .then(appointments => {
        return res.status(200).send({ appointments });
    })
    .catch(error => {
        return res.status(500).send({ error });
    })

});

module.exports = userAppts;
