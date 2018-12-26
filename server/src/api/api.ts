import express from 'express';

import { db } from '../db/knex';
import { Model } from 'objection';

import { authenticate } from './routes/authenticate';
import { services } from './routes/services';
import { user } from './routes/user/user';
import { admin } from './routes/admin/admin';

import { userMiddleware } from '../middlewares/user';
import { adminMiddleware } from '../middlewares/admin';

const api = express.Router();

Model.knex(db);

///////////////
// START OF API
///////////////

api.use('/authenticate', authenticate);
api.use('/services', services);

api.use('/user', userMiddleware, user);
api.use('/admin', userMiddleware, adminMiddleware, admin);

export = api;
