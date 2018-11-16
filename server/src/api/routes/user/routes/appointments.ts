import express from 'express'
import { Connection } from '../../../../db/knex'

const jwtWrapper = require('../../../../models/JWTWrapper');

const appointments = express.Router()
const knex = new Connection().knex()

appointments.get('/', jwtWrapper.verifyToken, (req, res) => {

    const userId = req.body.userId;
    let today = new Date();
    today = new Date(`${today.getFullYear()}/${today.getMonth() + 1}/${today.getDate()}`);

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

module.exports = appointments;
