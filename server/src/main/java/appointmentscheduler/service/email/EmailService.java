package appointmentscheduler.service.email;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Properties;

@Service
public class EmailService {

    private Properties props;

    private String email = "schedulerTester123@outlook.com";
    private String password = "testing123";
    private String logoPath = "images/asapp_logo.png";

    public EmailService() {
        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.outlook.com");
        props.put("mail.smtp.port", "587");
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    private Session getSession() {
        return Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });
    }

    private void addLogo(MimeMessage msg, String bodyContent) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(
                msg,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
        );

        helper.addAttachment("asapp_logo.png", new ClassPathResource(logoPath));
        String inlineImage = "<img src=\"cid:asapp_logo.png\" width=\"10%\" height=\"10%\"></img><br/>";
        helper.setText(bodyContent + "<p><br /><br />All the best, <br /> ASApp Team <br /></p>" + inlineImage, true);
    }


    public boolean sendEmail(String receiver, String subject, String content, boolean attachLogo) throws MessagingException {
        Session session = getSession();
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(email, false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
        msg.setSubject(subject);
        msg.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(content, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        if (attachLogo) {
            addLogo(msg, content);
        } else {
            msg.setContent(content, "text/html");
        }

        Transport.send(msg);

        return true;
    }

}