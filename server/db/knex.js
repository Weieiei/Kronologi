const config = require('../knexfile'),
    knex = require('knex'),
    environment = process.env.NODE_ENV || 'development';

module.exports = knex(config[environment]);
