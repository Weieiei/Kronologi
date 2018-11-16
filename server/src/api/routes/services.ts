import express from 'express'
let knex = require('../../db/knex'),
    Service = require('../../models/service/service');

let services = express.Router();

/**
 * @route       api/routes/services
 * @description Get services for a buisiness
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
    })

});

module.exports = services;
