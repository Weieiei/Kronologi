var nodemailer = require('nodemailer');
//values to be changed with accounts of admin sender + client receiver
var transporter = nodemailer.createTransport({
	  service: 'gmail',
	  auth: {
		      user: 'sender@gmail.com',
		      pass: 'passsword'
		    }
});

var mailOptions = {
	  from: 'sender@gmail.com',
	  to: 'receiver@gmail.com',
	  subject: 'Sending Email using Node.js',
	  text: 'That was easy!'
};

transporter.sendMail(mailOptions, function(error, info){
	  if (error) {
		      console.log(error);
		    } else {
			        console.log('Email sent: ' + info.response);
			      }
});
