import express from 'express';
import moment from 'moment';
import { Appointment } from '../../../../models/appointment/Appointment';
import { Service } from '../../../../models/service/Service';
import { RequestWrapper } from '../../../../wrappers/RequestWrapper';

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
    let startTime: string = req.body.start_time;
    const notes: string = req.body.notes;

    const service = await Service
        .query()
        .where({ id: serviceId })
        .first();

    if (!service) res.status(404).send({ error: 'Service not found.' });

    let endTime: string = moment(startTime).add(service.duration, 'm').format('YYYY-MM-DD HH:mm:ss');

    startTime += '-05';
    endTime += '-05';

    try {

        await Appointment
            .query()
            .insert({ userId, serviceId, startTime, endTime, notes });

        res.status(200).send({ message: 'Successfully booked.' });

    }
    catch (error) {
        res.status(500).send({ error });
    }

});

module.exports = appointments;
