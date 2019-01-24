package appointmentscheduler.seed;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.phonenumber.PhoneNumber;
import appointmentscheduler.entity.role.Role;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.entity.verification.Verification;
import appointmentscheduler.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
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
    private VerificationRepository verificationRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @EventListener
    public void seed(ContextRefreshedEvent event) throws NoSuchAlgorithmException {

        boolean noAdmin = userRepository.findByRoles_Role(RoleEnum.ADMIN).isEmpty();

        if (noAdmin) {
            seedAdminAndClients();
            seedEmployeeServicesAndShifts();
            seedAppointments();
        }

    }

    public void seedAdminAndClients() throws NoSuchAlgorithmException {

        Role adminRole = new Role(RoleEnum.ADMIN);
        Role clientRole = new Role(RoleEnum.CLIENT);

        User admin = new User("Admin", "User", "admin@admin.com", hash("admin123"), true);
        admin.setRoles(Stream.of(adminRole, clientRole).collect(Collectors.toSet()));

        User client1 = new User("John", "Doe", "johndoe@johndoe.com", hash("johndoe123"));
        client1.setRoles(Stream.of(clientRole).collect(Collectors.toSet()));

        User client2 = new User("Test", "User", "test@test.com", hash("test123"));
        client2.setRoles(Stream.of(clientRole).collect(Collectors.toSet()));

        Verification verifyUser1 = new Verification(client1);
        Verification verifyUser2 = new Verification(client2);

        admin.setPhoneNumber(new PhoneNumber("1", "514", "5551234", admin));
        client1.setPhoneNumber(new PhoneNumber("1", "514", "5552345", client1));
        client2.setPhoneNumber(new PhoneNumber("1", "514", "5553456", client2));

        userRepository.saveAll(Arrays.asList(admin, client1, client2));
        verificationRepository.saveAll(Arrays.asList(verifyUser1, verifyUser2));

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

        Role employeeRole = new Role(RoleEnum.EMPLOYEE);

        User employee = new User("Employee", "User", "employee@employee.com", hash("employee123"), true);
        employee.setRoles(Stream.of(employeeRole).collect(Collectors.toSet()));
        employee.setPhoneNumber(new PhoneNumber("1", "514", "5554567", employee));

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
        List<Service> services = serviceRepository.findAll();

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
