const express = require('express'),
    knex = require('../../db/knex');

const services = express.Router();

services.get('', (req, res) => {

    knex.select().from('services')
      .then(services => {
        res.json(services);
      });
});

module.exports = services;
