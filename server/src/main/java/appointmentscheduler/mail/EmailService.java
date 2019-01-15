package appointmentscheduler.mail;




import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.MimeMessageHelper;

public class EmailService{
    @Autowired
    private Environment env;
    Properties props;
    String username = "schedulerTester123@outlook.com";
    String password = "testing123";
    String logoPath = "server/src/assets/images/asapp_logo.png";

    public EmailService () {
        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.outlook.com");
        props.put("mail.smtp.port", "587");
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    private Session getSession() {
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        return session;
    }

    private void addLogo(MimeMessage msg, String bodyContent) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper( msg,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        helper.addAttachment("asapp_logo.png", new File(logoPath));
        String inlineImage = "<img src=\"cid:asapp_logo.png\" width=\"10%\" height=\"10%\"></img><br/>";
        helper.setText(bodyContent + "<p><br /><br />All the best, <br /> ASApp Team <br /></p>" + inlineImage, true);
    }


    public boolean sendmail(String receiver, String subject, String content, boolean attachLogo) throws  MessagingException{
        Session sess = getSession();
        MimeMessage msg = new MimeMessage(sess);
        msg.setFrom(new InternetAddress(username , false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
        msg.setSubject(subject);
        msg.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(content, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        if(attachLogo)
            addLogo(msg, content);
        else
            msg.setContent(content, "text/html");
        Transport.send(msg);
        return true;
    }

}
