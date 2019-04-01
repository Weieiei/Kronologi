package appointmentscheduler.entity.user;

import appointmentscheduler.entity.business.Business;

public class UserFactory {

    public static User createUser(Class<? extends User> clazz, String firstName, String lastName, String email, String password) {
        final User user = createFromType(clazz);

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);

        return user;
    }

    public static User createUser(Business business,Class<? extends User> clazz, String firstName, String lastName,
                                  String email,
                                  String password
                                  ) {
        if(clazz == Employee.class){
            final Employee employee = (Employee) createFromType(clazz);
            employee.setFirstName(firstName);
            employee.setLastName(lastName);
            employee.setEmail(email);
            employee.setPassword(password);
            employee.setBusiness(business);
            return employee;
        }

        else {
            final User user = createFromType(clazz);

            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(password);

            return user;
        }
    }



    private static User createFromType(Class<? extends User> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("User cannot be created");
    }
    public static User createAdmin(Business business,Class<? extends User> clazz, String firstName, String lastName,
                                  String email,
                                  String password
    ) {

            final User user = createFromType(clazz);

            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(password);
            user.setBusiness(business);
            return user;

    }

    public static User createGuest(Class<? extends User> clazz, String firstName, String lastName, String email, String password) {
        final User guest = createFromType(clazz);

        guest.setFirstName(firstName);
        guest.setLastName(lastName);
        guest.setEmail(email);
        guest.setPassword(password);

        return guest;
    }
}

