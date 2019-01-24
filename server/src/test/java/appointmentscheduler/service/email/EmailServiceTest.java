package appointmentscheduler.service.email;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.mail.MessagingException;
import java.io.IOException;

public class EmailServiceTest {

    private EmailService emailService;

    private String user = "schedulerTester123@outlook.com";
    private String pass = "testing123";

    @Before
    public void before() {
        emailService = new EmailService();
    }

    @Test
    public void testEmailDefault() throws IOException, MessagingException {
        //verify if credentials set to default
        Assert.assertEquals(user, emailService.getEmail());
        Assert.assertEquals(pass, emailService.getPassword());
    }

    @Test
    public void testEmailSending() throws IOException, MessagingException {
        Assert.assertTrue(emailService.sendEmail("schedulerTester123@outlook.com", "test email", "email testing", false));
    }

}
