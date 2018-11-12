const express = require('express'),
    jwtWrapper = require('../../../../models/JWTWrapper'),
    knex = require('../../../../db/knex');

const appointments = express.Router();

appointments.get('/', jwtWrapper.verifyToken, (req, res) => {

    // TODO

});

module.exports = appointments;
