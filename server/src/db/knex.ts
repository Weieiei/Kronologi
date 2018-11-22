import knex = require("knex");
import { Config } from 'knex';

// TODO getInstance()

/**
 * Class to set up the knex connection to allow access to our db instance
 */
export class Connection {
    public knex(): knex {
        return knex(exportConfig());
    }
}

function exportConfig(): Config{
    const environment = process.env.NODE_ENV || 'development';
    return require('./knexfile')[environment];
}
