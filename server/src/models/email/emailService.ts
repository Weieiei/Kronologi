import * as nodemailer from 'nodemailer';

export class EmailService {

    private service: string;
    private userEmail: string;
    private password: string;
    private transporter;

    constructor() {
        this.service = process.env.EMAIL_SERVICE;
        this.userEmail = process.env.EMAIL;
        this.password = process.env.EMAIL_PASSWORD;
        this.updateTransport();
    }

    public updateAllCredentials(service: string, userEmail: string, password: string) {
        this.service = service;
        this.userEmail = userEmail;
        this.password = password;
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

    public getUserEmail(): string {
        return this.userEmail;
    }

    public setUserEmail(userEmail: string): void {
        this.userEmail = userEmail;
        this.updateTransport();
    }

    public getPassword(): string {
        return this.password;
    }

    public setPassword(password: string): void {
        this.password = password;
        this.updateTransport();
    }

    public getService(): string {
        return this.service;
    }

    public setService(service: string): void {
        this.service = service;
        this.updateTransport();
    }

    public sendEmail(receiver: string, mailSubject: string, body: string) {
        const mailOptions = {
            from: this.userEmail,
            to: receiver,
            subject: mailSubject,
            text: body
        };

        this.transporter.sendMail(mailOptions, function(error, info) {
            if (error) {
                console.log(error);
            } else {
                console.log('Email sent: ' + info.response);
            }
        });
    }


}