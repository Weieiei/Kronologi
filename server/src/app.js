const express = require('express'),
    bodyParser = require('body-parser'),
    cors = require('cors'),
    api = require('./api/api');

const PORT = process.env.PORT || 3000;
const app = express();

app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

app.use('/api', api);

app.listen(PORT, () => {
    console.log('Running on port', PORT);
});
