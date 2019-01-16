package appointmentscheduler.property;

import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(name = "secrets", value = "secret.properties", ignoreResourceNotFound = true)
public class TwilioProperties {

    @Value("${twilio.account.sid}")
    private String twilioAccountSID;

    @Value("${twilio.auth.token}")
    private String twilioAuthToken;

    @Value("${twilio.from.number}")
    private String twilioFromNumber;

    public String getTwilioAccountSID() {
        return twilioAccountSID;
    }

    public String getTwilioAuthToken() {
        return twilioAuthToken;
    }

    public PhoneNumber getTwilioFromNumber() {
        return new PhoneNumber(twilioFromNumber);
    }

    public String getRawTwilioFromNumber() {
        return twilioFromNumber;
    }

}
