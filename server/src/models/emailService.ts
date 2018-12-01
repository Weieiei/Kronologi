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