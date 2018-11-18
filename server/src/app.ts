import  express from "express";
import * as bodyParser from "body-parser";
var api = require('./api/api');
var cors = require('cors');

const Gelf = require('gelf')
const gelf = new Gelf()

gelf.on('error', (err) => {
    console.log('ouch!', err)
  })

const PORT = process.env.PORT || 3000;
const app = express();
gelf.emit('gelf.log', 'myshortmessage')

// send a full message
const message = {
    "version": "1.0",
    "host": "www1",
    "short_message": "Short message",
    "full_message": "Backtrace here\n\nmore stuff",
    "timestamp": Date.now() / 1000,
    "level": 1,
    "facility": "payment-backend",
    "file": "/var/www/somefile.rb",
    "line": 356,
    "_user_id": 42,
    "_something_else": "foo"
  }
  
  gelf.emit('gelf.log', message);
  
app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

app.use('/api', api);

app.listen(PORT, () => {
    console.log('Running on port', PORT);
});