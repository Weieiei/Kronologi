import express from 'express';
import { Connection } from './../../db/knex';
import { Service } from '../../models/service/service';

const services = express.Router();
const knex = new Connection().knex();
/**
 * @route       GET api/services
 * @description Get list of services offered.
 * @access      Public
 */
services.get('', (req, res) => {

    knex.select().from('services')
    .then(resultSet => {

        const services = [];

        resultSet.forEach(row => {
            const service = new Service(row.id, row.name, row.duration);
            services.push(service);
        });

        return res.status(200).send(services);

    })
    .catch(error => {
        return res.status(500).send({ error });
    });

});

module.exports = services;
