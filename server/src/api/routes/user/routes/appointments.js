const express = require('express');
const knex = require('../../../../db/knex');
const moment = require('moment');

const appointments = express.Router();

appointments.get('/', (req, res) => {
    const userId = req.userId;
    let today = new Date();
    today = `${today.getFullYear()}/${today.getMonth() + 1}/${today.getDate()}`;

    knex.select('appointments.id', 'service_id', 'services.name', 'start_time', 'end_time', 'duration', 'notes')
        .from('appointments')
        .innerJoin('services', 'appointments.service_id', 'services.id')
        .where('appointments.user_id', userId)
        .andWhereRaw(`appointments.start_time >= '${today}'::date`)
        .then(appointments => {
            return res.status(200).send({appointments});
        })
        .catch(error => {
            return res.status(500).send({error});
        });
});

appointments.post('/', (req, res) => {
    const user_id = req.userId;
    const service_id = req.body.service_id;
    let start_time = req.body.start_time;
    const notes = req.body.notes;

    knex.select()
        .from('services')
        .where('id', service_id)
        .first()
        .then(service => {
            if (service !== undefined) {
                let end_time = moment(start_time).add(service.duration, 'm').format('YYYY-MM-DD HH:mm:ss');

                start_time += '-05';
                end_time += '-05';

                knex('appointments')
                    .whereBetween('start_time', [start_time, end_time])
                    .orWhereBetween('end_time', [start_time, end_time])
                    .then(exists => {
                        if (exists.length === 0) {
                            knex('appointments').insert({ user_id, service_id, start_time, end_time, notes }).then(result => {
                                res.send(result);
                            }).catch(err => {
                                res.status(400).send({ error: 'Bad request.' });
                            });
                        }
                        else {
                            res.status(409).send({ error: 'Conflict.' });
                        }
                    });


            }
            else {
                res.status(404).send({ error: 'Not found.' });
            }
        });
});

module.exports = appointments;
