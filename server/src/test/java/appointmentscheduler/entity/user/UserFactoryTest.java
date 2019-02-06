package appointmentscheduler.entity.user;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserFactoryTest {

    @Test
    public void createUser() {
        User user = UserFactory.createUser(User.class, "firstName", "lastName", "email@email.com", "password");

        assertEquals(User.class.getName(), user.getClass().getName());
    }

    @Test
    public void createEmployee() {
        User employee = UserFactory.createUser(Employee.class, "firstName", "lastName", "email@email.com", "password");

        assertEquals(Employee.class.getName(), employee.getClass().getName());
    }
}