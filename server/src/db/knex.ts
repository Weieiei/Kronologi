import knex from 'knex';
import * as knexfile from './knexfile';

console.log((knexfile[process.env.NODE_ENV]).connection);
export const db: knex = knex(knexfile[process.env.NODE_ENV]);
