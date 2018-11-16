import  express from "express";
import * as bodyParser from "body-parser";
var api = require('./api/api');
var cors = require('cors');

const PORT = process.env.PORT || 3000;
const app = express();

app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

app.use('/api', api);

app.listen(PORT, () => {
    console.log('Running on port', PORT);
});