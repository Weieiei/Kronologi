const express = require('express'),
    authenticate = require('./routes/authenticate'),
    services = require('./routes/services'),
    admin = require('./routes/admin/admin'),
    user = require('./routes/user/user');

const api = express.Router();

api.use('/authenticate', authenticate);
api.use('/services', services);
api.use('/admin', admin);
api.use('/user', user);

module.exports = api;
