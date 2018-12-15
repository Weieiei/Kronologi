import express from 'express';

const authenticate = require('./routes/authenticate');
const services = require('./routes/services');

const user = require('./routes/user/user');
const admin = require('./routes/admin/admin');

import { Model } from 'objection';
import { Connection } from '../db/knex';

import { userMiddleware } from '../middlewares/user';
import { adminMiddleware } from '../middlewares/admin';

const api = express.Router();

Model.knex(new Connection().knex());

///////////////
// START OF API
///////////////

api.use('/authenticate', authenticate);
api.use('/services', services);

api.use('/user', userMiddleware, user);
api.use('/admin', userMiddleware, adminMiddleware, admin);

module.exports = api;
