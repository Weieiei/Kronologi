const express = require('express'),
    jwtWrapper = require('../../models/JWTWrapper'),
    knex = require('../../db/knex'),
    Appointment = require('../../models/appointment/appointment');

const appointments = express.Router();

appointments.get('/user', jwtWrapper.verifyToken, (req, res) => {

    const userId = req.userId;

    knex.select().from('appointments').where('user_id', userId)
    .then(resultSet => {

        const appointments = [];

        resultSet.forEach(row => {
            const appointment = new Appointment(
                row.id, row.user_id, row.service_id,row.start_time, row.end_time, row.notes
            );
            appointments.push(appointment);
        });

        return res.status(200).send(appointments);

    })
    .catch(error => {
        return res.status(500).send({ error });
    })

});

module.exports = appointments;
