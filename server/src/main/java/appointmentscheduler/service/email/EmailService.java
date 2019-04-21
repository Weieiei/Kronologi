package appointmentscheduler.service.email;

import appointmentscheduler.property.EmailServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
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

    private String email;
    private String password;
    private EmailServiceProperties emailServiceProperties;

    private String registerSubject = "Kronologi Registration Confirmation";
    private String passwordResetSubject = "Kronologi Password Reset Confirmation";
    private String logoPath = "images/kronologi-logo-1.png";

    @Autowired
    public EmailService(EmailServiceProperties emailServiceProperties) {
        props = new Properties();
        this.emailServiceProperties = emailServiceProperties;
        this.email = emailServiceProperties.getEmailServiceId();
        this.password = emailServiceProperties.getEmailServicePassword();
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

        helper.addAttachment("kronologi-logo-1.png", new ClassPathResource(logoPath));
        String inlineImage = "<img src=\"cid:kronologi-logo-1.png\" width=\"10%\" height=\"10%\"></img><br/>";
        helper.setText(bodyContent + "<p><br /><br />All the best, <br /> Kronologi Team <br /></p>" + inlineImage, true);
    }

    public boolean sendRegistrationEmail(String receiver, String hash, boolean attachLogo) throws MessagingException {
        return sendEmail(receiver, this.registerSubject, generateRegistrationMessage(hash), attachLogo);

    }

    public boolean sendPasswordResetEmail(String receiver, String token, String email, boolean attachLogo) throws MessagingException {
        return sendEmail(receiver, this.passwordResetSubject, generatePasswordResetMessage(token, email), attachLogo);

    }

    public String generateRegistrationMessage(String hash)
    {
        String message = "Welcome to Kronologi! Please Confirm your email by clicking on the button below.<br />" ;
        String button = "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "  <tr>\n" +
                "      <td>\n" +
                "          <table cellspacing=\"0\" cellpadding=\"0\">\n" +
                "              <tr>\n" +
                "                  <td style=\"border-radius: 2px;\" bgcolor=\"#ED2939\">\n" +
                "                      <a href=\"http://localhost:4200/verification?hash=" + hash + "\" target=\"_blank\" style=\"padding: 8px 12px; border: 1px solid #ED2939;border-radius: 2px;font-family: Helvetica, Arial, sans-serif;font-size: 14px; color: #ffffff;text-decoration: none;font-weight:bold;display: inline-block;\">\n" +
                "                          Confirm Email             \n" +
                "                      </a>\n" +
                "                  </td>\n" +
                "              </tr>\n" +
                "          </table>\n" +
                "      </td>\n" +
                "  </tr>\n" +
                "</table>";
        return message + button;
    }

    public String generatePasswordResetMessage(String token, String email)
    {
        String message = "You recently requested a password reset. Please click on the button below to reset your password in the next 24 hours.<br />" ;
        String button = "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "  <tr>\n" +
                "      <td>\n" +
                "          <table cellspacing=\"0\" cellpadding=\"0\">\n" +
                "              <tr>\n" +
                "                  <td style=\"border-radius: 2px;\" bgcolor=\"#ED2939\">\n" +
                "                      <a href=\"http://localhost:4200/password/reset?token=" + token + "&email=" + email + "\" target=\"_blank\" style=\"padding: 8px 12px; border: 1px solid #ED2939;border-radius: 2px;font-family: Helvetica, Arial, sans-serif;font-size: 14px; color: #ffffff;text-decoration: none;font-weight:bold;display: inline-block;\">\n" +
                "                          Reset Password             \n" +
                "                      </a>\n" +
                "                  </td>\n" +
                "              </tr>\n" +
                "          </table>\n" +
                "      </td>\n" +
                "  </tr>\n" +
                "</table>";
        return message + button;
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
