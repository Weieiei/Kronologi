import { Appointment } from '../models/appointment/Appointment';
import { RequestWrapper } from '../wrappers/RequestWrapper';
import { ValidationError } from 'objection';

export const getMyAppointments = async (req: RequestWrapper, res) => {

    const userId: number = req.userId;
    const today: Date = new Date();
    const todayString: string = `${today.getFullYear()}/${today.getMonth() + 1}/${today.getDate()}`;

    try {

        const appointments = await Appointment
            .query()
            .where({ userId })
            .andWhereRaw(`appointments.start_time >= '${todayString}'::date`)
            .eager('[customer, employee, service]');

        return res.status(200).send(appointments);

    }
    catch (error) {
        return res.status(500).send({ error });
    }

};

export const bookAppointment = async (req: RequestWrapper, res) => {

    const userId: number = req.userId;
    const { employeeId, serviceId, startTime, notes } = req.body;

    try {

        await Appointment
            .query()
            .insert({ userId, employeeId, serviceId, startTime, notes });

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

};

export const getAllAppointments = async (req, res) => {

    const appointments = await Appointment
        .query()
        .eager('[customer, employee, service]');

    res.status(200).send(appointments);

};
