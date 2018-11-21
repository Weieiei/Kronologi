const graylog2 = require('graylog2');

export class Logger{

    private static _logger : Logger;  
    private graylog;

    private constructor() {

        this.graylog = new graylog2.graylog({
            servers: [
                { 'host': 'graylog', port: 12201}
            ],
            hostname: 'server.name', // the name of the client
                                     // (optional, default: os.hostname())
            facility: 'Node.js',     // the facility for these log messages
                                     // (optional, default: "Node.js")
            bufferSize: 1350         // max UDP packet size, should never exceed the
                                     // MTU of your system (optional, default: 1400)
        });
        
    }

    public static get Instance(): Logger {
        return this._logger || (this._logger = new Logger());
    }

    public getGrayLog() : any {
        return this.graylog;
    }

}
