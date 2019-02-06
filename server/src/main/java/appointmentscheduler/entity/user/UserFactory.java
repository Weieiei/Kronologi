package appointmentscheduler.entity.user;

public class UserFactory {

    public static User createUser(Class<? extends User> clazz, String firstName, String lastName, String email, String password) {
        final User user = createFromType(clazz);

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);

        return user;
    }

    private static User createFromType(Class<? extends User> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("User cannot be created");
    }
}
