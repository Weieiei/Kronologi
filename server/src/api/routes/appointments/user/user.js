const express = require('express'),
    jwtWrapper = require('../../../../models/JWTWrapper'),
    knex = require('../../../../db/knex');

const userAppts = express.Router();

userAppts.get('/', jwtWrapper.verifyToken, (req, res) => {

    const userId = req.userId;
    let today = new Date();
    today = `${today.getFullYear()}/${today.getMonth() + 1}/${today.getDate()}`;

    knex
        .select('appointments.id', 'service_id', 'services.name', 'start_time', 'end_time', 'duration', 'notes')
        .from('appointments')
        .innerJoin('services', 'appointments.service_id', 'services.id')
        .where('appointments.user_id', userId)
        .andWhereRaw(`appointments.start_time >= '${today}'::date`)
    .then(appointments => {
        return res.status(200).send({ appointments });
    })
    .catch(error => {
        return res.status(500).send({ error });
    })

});

module.exports = userAppts;
