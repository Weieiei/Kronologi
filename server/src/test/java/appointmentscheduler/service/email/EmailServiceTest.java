package appointmentscheduler.service.email;

import appointmentscheduler.property.EmailServiceProperties;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.mail.MessagingException;
import java.io.IOException;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {

    private EmailService emailService;

    @Mock
    private EmailServiceProperties emailServiceProperties;

    @Before
    public void before() {
        when(emailServiceProperties.getEmailServiceId()).thenReturn("schedulerTester123@outlook.com");
        when(emailServiceProperties.getEmailServicePassword()).thenReturn("testing123");
        emailService = new EmailService(emailServiceProperties);
    }

    @Test
    public void testEmailDefault() throws IOException, MessagingException {
        //verify if credentials set to default
        Assert.assertEquals(emailServiceProperties.getEmailServiceId(), emailService.getEmail());
        Assert.assertEquals(emailServiceProperties.getEmailServicePassword(), emailService.getPassword());
    }

    @Test
    public void testEmailSending() throws IOException, MessagingException {
        Assert.assertTrue(emailService.sendEmail("schedulerTester123@outlook.com", "test email", "email testing", false));
    }

}
