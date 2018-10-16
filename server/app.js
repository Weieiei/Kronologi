const express = require('express'), 
    bodyParser = require('body-parser'),
    cors = require('cors'),
    db = require('./db'),
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
 * Just used to test connection to database.
 */
db.query('SELECT * FROM users LIMIT 1;', [], (err, res) => {
    if (err) {
        console.log(err);
        console.log('DB connection unsuccessful.');
    }
    else {
        console.log(res.rows);
    }
})

app.listen(PORT, () => {
    console.log('Running on port', PORT);
})
