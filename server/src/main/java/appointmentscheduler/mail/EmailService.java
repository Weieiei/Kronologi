package appointmentscheduler.mail;




import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.MimeMessageHelper;

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
        Resource resource = new ClassPathResource("");
        System.out.println(resource.getFile().getAbsolutePath());
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("schedulerTester123@outlook.com", "testing123");
            }
        });
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("schedulerTester123@outlook.com", false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("schedulerTester123@outlook.com"));
        msg.setSubject("Tutorials point email");
        msg.setContent("Tutorials point email", "text/html");
        msg.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent("Tutorials point email", "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
       // MimeBodyPart attachPart = new MimeBodyPart();

        //attachPart.attachFile("src/assets/images/asapp_logo.png");
        //multipart.addBodyPart(attachPart);
        MimeMessageHelper helper = new MimeMessageHelper( msg,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        helper.addAttachment("asapp_logo.png", new File("src/assets/images/asapp_logo.png"));
        String inlineImage = "<img src=\"cid:asapp_logo.png\"></img><br/>";
        helper.setText(messageBodyPart.getContent()+ inlineImage, true);
        //msg.setContent(multipart);
        Transport.send(msg);
    }
}
