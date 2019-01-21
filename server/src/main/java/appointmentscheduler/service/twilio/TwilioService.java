package appointmentscheduler.service.twilio;

import appointmentscheduler.property.TwilioProperties;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    private TwilioProperties twilioProperties;

    @Autowired
    public TwilioService(TwilioProperties twilioProperties) {
        this.twilioProperties = twilioProperties;
    }

    public boolean sendTextMessage(String to, String textMessage) {

        try {

            Twilio.init(twilioProperties.getTwilioAccountSID(), twilioProperties.getTwilioAuthToken());
            Message message = Message.creator(new PhoneNumber(to), twilioProperties.getTwilioFromNumber(), textMessage).create();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

}
