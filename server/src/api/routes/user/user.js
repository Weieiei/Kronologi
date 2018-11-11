const express = require('express');

const appointments = require('./routes/appointment');

const router = express.Router();

router.use('/appointments', appointments);

module.exports = router;
