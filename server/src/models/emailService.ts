var nodemailer = require('nodemailer');

export class EmailService {
    private service;
    private userEmail;
    private password;
    private transporter;


    constructor(serv: string, user: string, pass: string) {
        this.service = serv;
        this.userEmail = user;
        this.password = pass;
        this.transporter = nodemailer.createTransport({
            service: this.service,
            auth: {
                    user: this.userEmail,
                    pass: this.password 
                  }
      });
    }

}