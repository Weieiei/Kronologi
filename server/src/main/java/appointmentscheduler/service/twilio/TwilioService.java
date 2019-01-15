package appointmentscheduler.service.twilio;

import appointmentscheduler.property.TwilioProperties;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    @Autowired
    private TwilioProperties properties;

    public ResponseEntity<?> sendTextMessage(String textMessage) {

        try {

            Twilio.init(properties.getTwilioAccountSID(), properties.getTwilioAuthToken());
            Message message = Message.creator(new PhoneNumber("+15149954485"), properties.getTwilioFromNumber(), textMessage).create();

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

}
