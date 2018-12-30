import knex from 'knex';
import * as knexfile from './knexfile';

export const db: knex = knex(knexfile[process.env.NODE_ENV]);
