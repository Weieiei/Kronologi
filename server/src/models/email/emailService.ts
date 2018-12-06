import nodemailer from 'nodemailer';
import * as config from './config.json';

export class EmailService {
    private service: string;
    private userEmail: string;
    private password: string;
    private transporter;



    constructor() {
        //set to default credentials(credentials of test email account)
        this.service = config.service;
        this.userEmail = config.email;
        this.password = config.password;
        this.updateTransport();
    }

    public updateAllCredentials(serv: string, user: string, pass: string) {
        this.service = serv;
        this.userEmail = user;
        this.password = pass;
        this.updateTransport();
    }

    private updateTransport() {
        this.transporter = nodemailer.createTransport({
            service: this.service,
            auth: {
                    user: this.userEmail,
                    pass: this.password 
                  }
      });
    }

    public getUserEmail():string {
        return this.userEmail;
    }

    public setUserEmail(user: string):void {
        this.userEmail = user;
        this.updateTransport();
    }

    public getPassword():string {
        return this.password;
    }

    public setPassword(pass: string):void {
        this.password = pass;
        this.updateTransport();
    }

    public getService():string {
        return this.service;
    }
    
    public setService(serv: string):void {
        this.service = serv;
        this.updateTransport();
    }

    public sendEmail(receiver: string, mailSubject: string, body : string) {
        var mailOptions = {
            from: this.userEmail,
            to: receiver,
            subject: mailSubject,
            text: body
      };

      this.transporter.sendMail(mailOptions, function(error, info){
        if (error) {
                console.log(error);
              } else {
                      console.log('Email sent: ' + info.response);
                    }
        });
    }


}