const express = require('express'),
    jwtWrapper = require('../../../../models/JWTWrapper'),
    knex = require('../../../../db/knex');

const adminAppts = express.Router();

adminAppts.get('/admin', jwtWrapper.verifyToken, (req, res) => {

    // TODO

});

module.exports = adminAppts;
