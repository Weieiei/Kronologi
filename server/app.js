const express = require('express'),
    bodyParser = require('body-parser'),
    cors = require('cors'),
    knex = require('./db/knex'),
    api = require('./api/api');

const PORT = process.env.PORT || 3000;
const app = express();

app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

/**
 * API endpoints are grouped up in the ./api/api.js file.
 */
app.use('/api', api);

/**
 * Dummy query.
 */
knex.select().from('users').limit(1).then(user => {
    console.log(user);
})

app.listen(PORT, () => {
    console.log('Running on port', PORT);
})
