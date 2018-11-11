import  * as express from "express";
import * as bodyParser from "body-parser";
import { api } from './api/api';
var cors = require('cors');

export class App{
    public app: express.Application;

        /**
     * Bootstrap the application.
     *
     * @class WebPortal
     * @method bootstrap
     * @static
     * @return {ng.auto.IInjectorService} Returns the newly created injector for this app.
     */
    public static bootstrap(): App {
        return new App();
    }


    constructor(){
        this.app = express()
        this.mountRoutes()
    }
    
    private mountRoutes (): void {

        this.app.use(cors());
        //use json form parser middlware
        this.app.use(bodyParser.json());

        //use query string parser middlware
        this.app.use(bodyParser.urlencoded({
            extended: true
        }));

        this.app.use('/api', api)

                /**
         * Dummy query.
         */
  //      new Connection().knex().select().from('users').limit(1).then(user => {
      //      console.log(user);
      //  });
    }
}

export default new App().app