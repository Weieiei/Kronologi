package appointmentscheduler.mail;

import org.junit.Assert;
import org.junit.Test;

import javax.mail.MessagingException;
import java.io.IOException;

public class MailTest {
    EmailService mail = new EmailService();
    @Test
    public void testConstructor() throws IOException, MessagingException {
        mail.sendmail();
    }
}
