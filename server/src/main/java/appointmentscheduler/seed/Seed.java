package appointmentscheduler.seed;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.role.Role;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.service.ServiceEntity;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        boolean noAdmin = userRepository.findByRoles_Role(RoleEnum.ADMIN).isEmpty();

        if (noAdmin) {
            seedAdminAndClients();
            seedEmployeeServicesAndShifts();
            seedAppointments();
        }

    }

    public void seedAdminAndClients() {

        Role adminRole = new Role(RoleEnum.ADMIN);
        Role clientRole = new Role(RoleEnum.CLIENT);

        User admin = new User("Admin", "User", "admin@admin.com", hash("admin123"));
        admin.setRoles(Stream.of(adminRole, clientRole).collect(Collectors.toSet()));

        User client1 = new User("John", "Doe", "johndoe@johndoe.com", hash("johndoe123"));
        client1.setRoles(Stream.of(clientRole).collect(Collectors.toSet()));

        User client2 = new User("Test", "User", "test@test.com", hash("test123"));
        client2.setRoles(Stream.of(clientRole).collect(Collectors.toSet()));

        userRepository.saveAll(Arrays.asList(admin, client1, client2));

    }

    public void seedEmployeeServicesAndShifts() {

        List<ServiceEntity> services = new ArrayList<>();

        services.add(new ServiceEntity("BACK TO PURE LIFE", 150));
        services.add(new ServiceEntity("QUICK VISIT TO THE SPA", 100));
        services.add(new ServiceEntity("BUSINESS ONLY", 120));
        services.add(new ServiceEntity("MEC EXTRA", 200));
        services.add(new ServiceEntity("GET BACK ON TRACK", 170));
        services.add(new ServiceEntity("SLENDER QUEST", 200));
        services.add(new ServiceEntity("SERENITY", 140));
        services.add(new ServiceEntity("ULTIMATE ESCAPE", 160));
        services.add(new ServiceEntity("DIVINE RELAXATION", 150));
        services.add(new ServiceEntity("1/2 DAY PASSPORT", 165));
        services.add(new ServiceEntity("THE FULL DAY PASSPORT", 325));
        services.add(new ServiceEntity("POETRY FOR TWO", 180));
        services.add(new ServiceEntity("ULTIMATE COUPLES TREAT", 320));
        services.add(new ServiceEntity("BODY AFTER BABY", 120));
        services.add(new ServiceEntity("LOST YOUR SOUL", 120));
        services.add(new ServiceEntity("RECONNECT WITH YOUR BODY", 210));

        serviceRepository.saveAll(services);

        Role employeeRole = new Role(RoleEnum.EMPLOYEE);

        User employee = new User("Employee", "User", "employee@employee.com", hash("employee123"));

        employee.setRoles(Stream.of(employeeRole).collect(Collectors.toSet()));

        employee.setEmployeeServices(Arrays.asList(
                services.get(0), services.get(1), services.get(3),
                services.get(6), services.get(8), services.get(11)
        ));

        List<Shift> shifts = new ArrayList<>();
        shifts.add(new Shift(employee, LocalDate.of(2019, 11, 30), LocalTime.of(12, 0), LocalTime.of(21, 0)));
        shifts.add(new Shift(employee, LocalDate.of(2019, 12, 2), LocalTime.of(12, 0), LocalTime.of(21, 0)));

        userRepository.save(employee);
        shiftRepository.saveAll(shifts);

    }

    public void seedAppointments() {

        List<User> clients = userRepository.findByRoles_Role(RoleEnum.CLIENT);
        User employee = userRepository.findByRoles_Role(RoleEnum.EMPLOYEE).get(0);
        List<ServiceEntity> services = serviceRepository.findAll();

        Appointment appointment = new Appointment(
                clients.get(0), employee, services.get(11),
                LocalDate.of(2019, 11, 30), LocalTime.of(12, 0), "Some note"
        );

        Appointment appointment2 = new Appointment(
                clients.get(1), employee, services.get(6),
                LocalDate.of(2019, 12, 2), LocalTime.of(12, 0), "Some note"
        );

        appointmentRepository.saveAll(Arrays.asList(appointment, appointment2));

    }

    public String hash(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

}
