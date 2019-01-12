package appointmentscheduler.mail;




import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class EmailService{
    @Autowired
    private Environment env;
    Properties props;
    String username;

    EmailService () {
        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.outlook.com");
        props.put("mail.smtp.port", "587");
    }
    public void sendmail() throws AddressException, MessagingException, IOException {

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("schedulerTester123@outlook.com", "testing123");
            }
        });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("schedulerTester123@outlook.com", false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("schedulerTester123@outlook.com"));
        msg.setSubject("Tutorials point email");
        msg.setContent("Tutorials point email", "text/html");
        msg.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent("Tutorials point email", "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        MimeBodyPart attachPart = new MimeBodyPart();

        attachPart.attachFile("src/assets/images/asapp_logo.png");
        multipart.addBodyPart(attachPart);
        msg.setContent(multipart);
        Transport.send(msg);
    }
}
