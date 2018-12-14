import express from 'express';
import { Service } from '../../models/service/Service';

const services = express.Router();

/**
 * @route       GET api/services
 * @description Get list of services offered.
 * @access      Public
 */
services.get('', async (req, res) => {

    try {
        const services = await Service.query();
        return res.status(200).send(services);
    }
    catch (error) {
        return res.status(500).send({ error });
    }

});

module.exports = services;
