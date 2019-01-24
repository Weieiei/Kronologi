package appointmentscheduler.entity.phonenumber;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhoneNumberTest {

    @Test
    public void testRawPhoneNumber() {
        PhoneNumber phoneNumber = new PhoneNumber("1", "555", "1234");
        assertEquals("15551234", phoneNumber.getRawPhoneNumber());
    }

}
