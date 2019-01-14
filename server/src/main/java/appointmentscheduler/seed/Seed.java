package appointmentscheduler.seed;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.role.Role;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.room.Room;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.shift.ShiftFactory;
import appointmentscheduler.entity.user.Employee;
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
import java.util.*;
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
    private RoomRepository roomRepository;

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

        if (roomRepository.count() == 0) {
            seedRooms();
        }

    }

    public void seedAdminAndClients() {

        Role adminRole = new Role(RoleEnum.ADMIN);
        Role clientRole = new Role(RoleEnum.CLIENT);

        User admin = createUser("Admin", "User", "admin@admin.com", hash("admin123"));
        admin.setRoles(Stream.of(adminRole, clientRole).collect(Collectors.toSet()));

        User client1 = createUser("John", "Doe", "johndoe@johndoe.com", hash("johndoe123"));
        client1.setRoles(Stream.of(clientRole).collect(Collectors.toSet()));

        User client2 = createUser("Test", "User", "test@test.com", hash("test123"));
        client2.setRoles(Stream.of(clientRole).collect(Collectors.toSet()));

        userRepository.saveAll(Arrays.asList(admin, client1, client2));

    }

    private User createUser(String firstName, String lastName, String email, String passowrd) {
        User user = new User();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(passowrd);

        return user;
    }

    private Employee createEmployee(String firstName, String lastName, String email, String passowrd) {
        Employee user = new Employee();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(passowrd);

        return user;
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

        Employee employee = createEmployee("Employee", "User", "employee@employee.com", hash("employee123"));

        employee.setRoles(Stream.of(employeeRole).collect(Collectors.toSet()));

        Set<Service> set = new HashSet<>(Arrays.asList(
                services.get(0), services.get(1), services.get(3),
                services.get(6), services.get(8), services.get(11)
        ));
        employee.setServices(set);

        Set<Shift> shifts = new HashSet<>();
        shifts.add(ShiftFactory.createShift(LocalDate.of(2019, 11, 30), LocalTime.of(12, 0), LocalTime.of(21, 0)));
        shifts.add(ShiftFactory.createShift(LocalDate.of(2019, 12, 2), LocalTime.of(12, 0), LocalTime.of(21, 0)));

        employee.setShifts(shifts);

        userRepository.save(employee);

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

    public void seedRooms() {
        Room room = new Room();
        room.setName("Laser Room");

        roomRepository.save(room);
    }
}
