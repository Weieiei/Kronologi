
import express from 'express'
import { Admin } from '../models/user/Admin'
const knex = require('../db/knex');
const jwtWrapper = require('../models/JWTWrapper');

const authenticate = require('./routes/authenticate');
const services = require('./routes/services');

const user = require('./routes/user/user');
const admin = require('./routes/admin/admin');

const api = express.Router();
const error = 'Unauthorized request.';

function userMiddleware(req, res, next) {
    if (!req.headers.authorization) {
        return res.status(401).send({ error });
    }
    
    let token = req.headers.authorization.split(' ')[1];
    if (token === 'null') {
        return res.status(401).send({ error });
    }
    let payload = jwtWrapper.verifyToken(token);
    if (!payload) {
        return res.status(401).send({ error });
    }
    req.body.userId = payload.subject;
    next();
}

function adminMiddleware(req, res, next) {
    const userId = req.body.userId;

    knex.select().from('users').where('id', userId).then(users => {
        if (users.length === 0) {
            res.status(401).send({ error });
        }
        else {
            const user = users[0];
            if (user.user_type === Admin.name) {
                next();
            }
            else {
                res.status(401).send({ error });
            }
        }
    });
}


///////////////
// START OF API
///////////////

api.use('/authenticate', authenticate);
api.use('/authenticate', authenticate);
api.use('/services', services);

api.use('/user', userMiddleware, user);
api.use('/admin', userMiddleware, adminMiddleware, admin);


module.exports = api;

