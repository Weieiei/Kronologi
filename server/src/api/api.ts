import express from 'express';
const jwtWrapper = require('../models/JWTWrapper');

const authenticate = require('./routes/authenticate');
const services = require('./routes/services');

const user = require('./routes/user/user');
const admin = require('./routes/admin/admin');

import { Admin } from '../models/user/Admin';

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
    if (req.userType !== Admin.getType()) {
        return res.status(401).send({ error });
    }

    next();
}


///////////////
// START OF API
///////////////

api.use('/authenticate', authenticate);
api.use('/services', services);

api.use('/user', userMiddleware, user);
api.use('/admin', userMiddleware, adminMiddleware, admin);

module.exports = api;
