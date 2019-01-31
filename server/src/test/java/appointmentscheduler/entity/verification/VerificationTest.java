package appointmentscheduler.entity.verification;

import appointmentscheduler.entity.user.User;
import org.junit.BeforeClass;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

public class VerificationTest {


    User user;
    Verification verification;

    public VerificationTest() throws NoSuchAlgorithmException {
        user = new User("Test", "Test", "Test@test.com", "test", false);
        verification = new Verification(user);
    }


    @Test
    public void getId() {
        assertEquals(0, verification.getId());
    }

    @Test
    public void setId() {
        verification.setId(2);
        assertEquals(2, verification.getId());
    }

    @Test
    public void getUser() {
        assertEquals("Test@test.com", verification.getUser().getEmail());
    }

    @Test
    public void setUser() {
        User user2 = new User("Blob", "Blob", "Blob@blob.com", "Blob", false);
        verification.setUser(user2);
        assertEquals("Blob@blob.com", verification.getUser().getEmail());
        verification.setUser(user);
    }

    @Test
    public void getHash() {
        assertFalse(user.getEmail() == verification.getHash());
    }

    @Test
    public void setHash() {
        verification.setHash("Test");
        assertEquals("Test", verification.getHash());
    }
}