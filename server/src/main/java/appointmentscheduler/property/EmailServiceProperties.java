package appointmentscheduler.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(name = "secrets", value = "secret.properties", ignoreResourceNotFound = true)
public class EmailServiceProperties {

    @Value("${emailService.id}")
    private String emailServiceId;

    @Value("${emailService.password}")
    private String emailServicePassword;

    public String getEmailServiceId() {
        return emailServiceId;
    }

    public String getEmailServicePassword() {
        return emailServicePassword;
    }
}
