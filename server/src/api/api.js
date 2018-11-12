const express = require('express');
const jwtWrapper = require('../models/JWTWrapper');

const authenticate = require('./routes/authenticate');
const services = require('./routes/services');
const appointment = require('./routes/appointment')

const user = require('./routes/user/user');

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

    req.userId = payload.subject;
    next();
}

///////////////
// START OF API
///////////////

api.get('/', (req, res) => {
    res.send({ message: 'Hey world' });
});

api.use('/authenticate', authenticate);
api.use('/services', services);
api.use('/appointment', appointment);

api.use('/user', userMiddleware, user);

module.exports = api;
