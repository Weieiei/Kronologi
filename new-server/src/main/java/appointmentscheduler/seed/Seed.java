package appointmentscheduler.seed;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.entity.user.UserType;
import appointmentscheduler.repository.AppointmentRepository;
import appointmentscheduler.repository.ServiceRepository;
import appointmentscheduler.repository.ShiftRepository;
import appointmentscheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class Seed {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @EventListener
    public void seed(ContextRefreshedEvent event) {

        boolean noAdmin = userRepository.findUserByUserType(UserType.admin).isEmpty();

        if (noAdmin) {
            seedAdminAndClients();
            seedEmployeeServicesAndShifts();
            seedAppointments();
        }

    }

    public void seedAdminAndClients() {

        User admin = new User(
                "Admin", "User",
                "admin@admin.com", hash("admin123"), UserType.admin
        );

        User client1 = new User(
                "John", "Doe",
                "johndoe@johndoe.com", hash("johndoe123"), UserType.client
        );

        User client2 = new User(
                "Test", "User",
                "test@test.com", hash("test123"), UserType.client
        );

        userRepository.saveAll(Arrays.asList(admin, client1, client2));

    }

    public void seedEmployeeServicesAndShifts() {

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

        User employee = new User(
                "Employee", "User",
                "employee@employee.com", hash("employee123"), UserType.employee
        );

        employee.setEmployeeServices(Arrays.asList(
                services.get(0), services.get(1), services.get(3),
                services.get(6), services.get(8), services.get(11)
        ));

        List<Shift> shifts = new ArrayList<>();
        shifts.add(new Shift(
                employee,
                LocalDateTime.of(2019, 11, 30, 12, 0),
                LocalDateTime.of(2019, 11, 30, 21, 0)
        ));
        shifts.add(new Shift(
                employee,
                LocalDateTime.of(2019, 12, 02, 12, 0),
                LocalDateTime.of(2019, 12, 01, 21, 0)
        ));

        userRepository.save(employee);
        shiftRepository.saveAll(shifts);

    }

    public void seedAppointments() {

        List<User> clients = userRepository.findUserByUserType(UserType.client);
        User employee = userRepository.findUserByUserType(UserType.employee).get(0);
        List<Service> services = serviceRepository.findAll();

        Appointment appointment = new Appointment(
                clients.get(0), employee, services.get(11),
                LocalDateTime.of(2019, 11, 30, 12, 0), "Some note"
        );

        Appointment appointment2 = new Appointment(
                clients.get(1), employee, services.get(6),
                LocalDateTime.of(2019, 12, 2, 12, 0), "Some note"
        );

        appointmentRepository.saveAll(Arrays.asList(appointment, appointment2));

    }

    public String hash(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

}
