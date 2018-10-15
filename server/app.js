const express = require('express'), 
    bodyParser = require('body-parser'),
    cors = require('cors'),
    db = require('./db');

const PORT = process.env.PORT || 3000;
const app = express();

app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

app.get('/api/cork', (req, res) => {
    res.send({ message: 'Hey world' });
})

db.query('SELECT $1::text AS greeting;', ['Hi Earth'], (err, res) => {
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
