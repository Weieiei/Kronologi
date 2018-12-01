var nodemailer = require('nodemailer');

export class EmailService {
    private service;
    private userEmail;
    private password;


    constructor(service: string, user: string, pass: string) {
        this.service = service;
        this.userEmail = user;
        this.password = pass;
    }

}