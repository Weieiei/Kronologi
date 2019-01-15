package appointmentscheduler.mail;

import org.junit.Assert;
import org.junit.Test;

import javax.mail.MessagingException;
import javax.mail.Session;
import java.io.IOException;
import static org.mockito.Mockito.*;

public class MailTest {
    EmailService mail = new EmailService();
    String user = "schedulerTester123@outlook.com";
    String pass = "testing123";

    @Test
    public void testEmailDefault() throws IOException, MessagingException {
        //verify if credentials set to default
        Assert.assertEquals(user, mail.getUsername());
        Assert.assertEquals(pass, mail.getPassword());
    }

    @Test
    public void testEmailSending() throws IOException, MessagingException {
        Assert.assertTrue(mail.sendmail("schedulerTester123@outlook.com", "test email", "email testing", false));
    }
}
