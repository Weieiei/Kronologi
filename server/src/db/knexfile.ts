import * as dotenv from 'dotenv';
import { knexSnakeCaseMappers } from 'objection';

// adding these 2 variables to have the full path, otherwise the file cannot be read when running the mocha tests
const fullpath = __dirname;
const path = fullpath.substring(0, fullpath.indexOf('AppointmentScheduler') + 'AppointmentScheduler'.length) + '/server/.env';

dotenv.config({ path });

module.exports = {

    test: {
        client: 'pg',
        connection: {
            host: process.env.DB_HOST,
            port: process.env.DB_PORT,
            user: process.env.DB_USER,
            password: process.env.DB_PASSWORD,
            database: process.env.DB_NAME_TEST
        },
        migrations: {
            directory: __dirname + '/migrations'
        },
        seeds: {
            directory: __dirname + '/seeds'
        },
        ...knexSnakeCaseMappers()
    },
    development: {
        client: 'pg',
        connection: {
            host: process.env.DB_HOST,
            port: process.env.DB_PORT,
            user: process.env.DB_USER,
            password: process.env.DB_PASSWORD,
            database: process.env.DB_NAME
        },
        migrations: {
            directory: __dirname + '/migrations'
        },
        seeds: {
            directory: __dirname + '/seeds'
        },
        ...knexSnakeCaseMappers()
    },
    production: {
        client: 'pg',
        connection: {
            host: process.env.DB_HOST,
            port: process.env.DB_PORT,
            user: process.env.DB_USER,
            password: process.env.DB_PASSWORD,
            database: process.env.DB_NAME
        },
        migrations: {
            directory: __dirname + '/migrations'
        },
        seeds: {
            directory: __dirname + '/seeds/production'
        },
        ...knexSnakeCaseMappers()
    }

};
