import { Service } from '../models/service/Service';

export const getAllServices = async (req, res) => {

    try {
        const services = await Service.query();
        return res.status(200).send(services);
    }
    catch (error) {
        return res.status(500).send({ error });
    }

};
