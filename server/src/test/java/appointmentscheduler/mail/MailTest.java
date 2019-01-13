package appointmentscheduler.mail;

import org.junit.Assert;
import org.junit.Test;

import javax.mail.MessagingException;
import java.io.IOException;

public class MailTest {
    EmailService mail = new EmailService();
    @Test
    public void testEmailSending() throws IOException, MessagingException {
        Assert.assertTrue(mail.sendmail("schedulerTester123@outlook.com", "test email", "email testing", false));
    }
}
