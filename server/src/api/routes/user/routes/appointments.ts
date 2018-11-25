
import express from 'express'
import { Connection } from '../../../../db/knex'
import moment from 'moment'

const appointments = express.Router()
const knex = new Connection().knex()

/**
 * @route       GET api/user/appointments
 * @description Get all of you appointments that are either today or in the future.
 * @access      Private  
 */
appointments.get('/', (req, res) => {

    const userId = req.userId;
    let today = new Date();
    let todayString : string = `${today.getFullYear()}/${today.getMonth() + 1}/${today.getDate()}`;

    knex.select('appointments.id', 'service_id', 'services.name', 'start_time', 'end_time', 'duration', 'notes')
        .from('appointments')
        .innerJoin('services', 'appointments.service_id', 'services.id')
        .where('appointments.user_id', userId)
        .andWhereRaw(`appointments.start_time >= '${todayString}'::date`)
        .then(appointments => {
            return res.status(200).send({appointments});
        })
        .catch(error => {
            return res.status(500).send({error});
        });

});

/**
 * @route       POST api/user/appointments
 * @description Make an appointment.
 * @access      Private  
 */
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
