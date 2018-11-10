import knex = require("knex");
import { Config } from 'knex';


export class Connection {
    public knex(): knex {
        return knex(exportConfig());
    }
}

function exportConfig(): Config{
    const environment = process.env.NODE_ENV || 'development';
    return require('./knexfile'[environment]);
    //# sourceMappingURL=knex.js.map
}
