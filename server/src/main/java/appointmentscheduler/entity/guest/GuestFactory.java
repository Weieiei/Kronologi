package appointmentscheduler.entity.guest;

public class GuestFactory {
    public static Guest createGuest(Class<? extends Guest> clazz, String firstName, String lastName, String email) {
        final Guest guest = createFromType(clazz);

        guest.setFirstName(firstName);
        guest.setLastName(lastName);
        guest.setEmail(email);

        return guest;
    }

    private static Guest createFromType(Class<? extends Guest> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Guest cannot be created");
    }

}
