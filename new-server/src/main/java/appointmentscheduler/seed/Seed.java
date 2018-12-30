package appointmentscheduler.seed;

import appointmentscheduler.entity.Service;
import appointmentscheduler.entity.User;
import appointmentscheduler.entity.UserType;
import appointmentscheduler.repository.ServiceRepository;
import appointmentscheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class Seed {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @EventListener
    public void seed(ContextRefreshedEvent event) {

        boolean adminExists = userRepository.findUserByUserType(UserType.admin) != null;

        if (!adminExists) {
            seedUsers();
            seedServices();
        }

    }

    public void seedUsers() {

        User admin = new User(
                "Admin", "User", "admin@admin.com",
                "admin", hash("admin123"), UserType.admin
        );

        User client1 = new User(
                "John", "Doe", "johndoe@johndoe.com",
                "johndoe", hash("johndoe123"), UserType.client
        );

        User client2 = new User(
                "Test", "User", "test@test.com",
                "test", hash("test123"), UserType.client
        );

        User employee = new User(
                "Employee", "User", "employee@employee.com",
                "employee", hash("employee123"), UserType.employee
        );

        userRepository.saveAll(Arrays.asList(admin, client1, client2, employee));

    }

    public void seedServices() {

        List<Service> services = new ArrayList<>();

        services.add(new Service("BACK TO PURE LIFE", 150));
        services.add(new Service("QUICK VISIT TO THE SPA", 100));
        services.add(new Service("BUSINESS ONLY", 120));
        services.add(new Service("MEC EXTRA", 200));
        services.add(new Service("GET BACK ON TRACK", 170));
        services.add(new Service("SLENDER QUEST", 200));
        services.add(new Service("SERENITY", 140));
        services.add(new Service("ULTIMATE ESCAPE", 160));
        services.add(new Service("DIVINE RELAXATION", 150));
        services.add(new Service("1/2 DAY PASSPORT", 165));
        services.add(new Service("THE FULL DAY PASSPORT", 325));
        services.add(new Service("POETRY FOR TWO", 180));
        services.add(new Service("ULTIMATE COUPLES TREAT", 320));
        services.add(new Service("BODY AFTER BABY", 120));
        services.add(new Service("LOST YOUR SOUL", 120));
        services.add(new Service("RECONNECT WITH YOUR BODY", 210));

        serviceRepository.saveAll(services);

    }

    public String hash(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

}
