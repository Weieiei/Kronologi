/*
import * as config from './config.json';

module.exports = {

    test: {
        client: 'pg',
        connection: {
            host: process.env.DB_HOST || config.host,
            port: config.port,
            user: config.user,
            password: config.password,
            database: "scheduler-test"
        },
        migrations: {
            directory: __dirname + '/migrations'
        },
        seeds: {
            directory: __dirname + '/seeds'
        }
    },
    development: {
        client: 'pg',
        connection: {
            host: process.env.DB_HOST || config.host,
            port: config.port,
            user: config.user,
            password: config.password,
            database: config.database
        },
        migrations: {
            directory: __dirname + '/migrations'
        },
        seeds: {
            directory: __dirname + '/seeds'
        }
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
        }
    }
};
*/
