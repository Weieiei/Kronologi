import express from 'express';
const jwtWrapper = require('../models/JWTWrapper');

const authenticate = require('./routes/authenticate');
const services = require('./routes/services');

const user = require('./routes/user/user');
const admin = require('./routes/admin/admin');

import { UserType } from '../models/user/UserType';
import { Model } from 'objection';
import { Connection } from '../db/knex';

const api = express.Router();
const error = 'Unauthorized request.';

function userMiddleware(req, res, next) {
    if (!req.headers.authorization) {
        return res.status(401).send({ error });
    }

    const token = req.headers.authorization.split(' ')[1];
    if (token === 'null') {
        return res.status(401).send({ error });
    }

    const payload = jwtWrapper.verifyToken(token);
    if (!payload) {
        return res.status(401).send({ error });
    }

    req.userId = payload.subject;
    req.userType = payload.type;

    next();
}

function adminMiddleware(req, res, next) {
    if (req.userType !== UserType.admin) {
        return res.status(401).send({ error });
    }

    next();
}

Model.knex(new Connection().knex());

///////////////
// START OF API
///////////////

api.use('/authenticate', authenticate);
api.use('/services', services);

api.use('/user', userMiddleware, user);
api.use('/admin', userMiddleware, adminMiddleware, admin);

module.exports = api;
