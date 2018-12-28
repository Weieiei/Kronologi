import { Appointment } from '../models/appointment/Appointment';
import { RequestWrapper } from '../wrappers/RequestWrapper';
import { ValidationError } from 'objection';
import { AppointmentStatus } from '../models/appointment/AppointmentStatus';

export const getMyAppointments = async (req: RequestWrapper, res) => {

    const userId: number = req.userId;
    const today: Date = new Date();
    const todayString: string = `${today.getFullYear()}/${today.getMonth() + 1}/${today.getDate()}`;

    try {

        const appointments = await Appointment
            .query()
            .where({ clientId: userId })
            .andWhereRaw(`appointments.start_time >= '${todayString}'::date`)
            .eager('[client, employee, service]');

        return res.status(200).send(appointments);

    } catch (error) {
        return res.status(500).send({ error });
    }

};

export const bookAppointment = async (req: RequestWrapper, res) => {

    const userId: number = req.userId;
    const { employeeId, serviceId, startTime, notes } = req.body;

    try {

        /**
         * Even though we get a Date object from the frontend, we still wrap it in a new Date() object so that it gets converted to UTC.
         */
        await Appointment
            .query()
            .insert({ clientId: userId, employeeId, serviceId, startTime: new Date(startTime), notes });

        res.status(200).send({ message: 'Successfully booked.' });

    } catch (error) {

        if (error instanceof ValidationError) {
            return res.status(400).send({ error: error.message });
        }
        else {
            res.status(500).send({ error });
        }

    }

};

export const cancelAppointment = async (req: RequestWrapper, res) => {

    const userId: number = req.userId;
    const { id } = req.params;

    try {

        const appointment = await Appointment.query()
            .patch({ status: AppointmentStatus.cancelled })
            .where({ id, clientId: userId, status: AppointmentStatus.confirmed })
            .first();

        if (appointment) {
            res.status(200).send({ message: 'Successfully cancelled appointment.' });
        } else {
            res.status(400).send({ error: 'Appointment not found.' });
        }

    } catch (error) {
        res.status(500).send({ error });
    }

};

export const getAllAppointments = async (req, res) => {

    const appointments = await Appointment
        .query()
        .eager('[client, employee, service]');

    res.status(200).send(appointments);

};
