package appointmentscheduler.entity.guest;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GuestFactoryTest {
    @Test
    public void createGuest() {
        Guest guest = GuestFactory.createGuest(Guest.class, "firstName", "lastName", "email@email.com");

        assertEquals(Guest.class.getName(), guest.getClass().getName());
    }

}
