import express from 'express';
import { Appointment } from '../../../../models/appointment/Appointment';
import { RequestWrapper } from '../../../../wrappers/RequestWrapper';
import { ValidationError } from 'objection';

const appointments = express.Router();

/**
 * @route       GET api/user/appointments
 * @description Get all of you appointments that are either today or in the future.
 * @access      Private
 */
appointments.get('/', async (req: RequestWrapper, res) => {

    const userId: number = req.userId;
    const today: Date = new Date();
    const todayString: string = `${today.getFullYear()}/${today.getMonth() + 1}/${today.getDate()}`;

    try {

        const appointments = await Appointment
            .query()
            .where({ userId })
            .andWhereRaw(`appointments.start_time >= '${todayString}'::date`)
            .eager('service');

        return res.status(200).send({ appointments });

    }
    catch (error) {
        return res.status(500).send({ error });
    }

});

/**
 * @route       POST api/user/appointments
 * @description Make an appointment.
 * @access      Private
 */
appointments.post('/', async (req: RequestWrapper, res) => {

    const userId: number = req.userId;
    const serviceId: number = req.body.service_id;
    const startTime: string = req.body.start_time;
    const notes: string = req.body.notes;

    try {

        await Appointment
            .query()
            .insert({ userId, serviceId, startTime, notes });

        res.status(200).send({ message: 'Successfully booked.' });

    }
    catch (error) {

        if (error instanceof ValidationError) {
            return res.status(400).send({ error: error.message });
        }
        else {
            res.status(500).send({ error });
        }

    }

});

module.exports = appointments;
