const express = require('express'),
    knex = require('../../db/knex'),
    Service = require('../../models/service/service');

const services = express.Router();

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
