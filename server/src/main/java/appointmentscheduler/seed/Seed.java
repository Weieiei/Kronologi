package appointmentscheduler.seed;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.appointment.AppointmentFactory;
import appointmentscheduler.entity.role.Role;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.room.Room;
import appointmentscheduler.entity.room.RoomFactory;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.service.ServiceFactory;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.shift.ShiftFactory;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.entity.user.UserFactory;
import appointmentscheduler.repository.*;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Seed {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

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

    }

    public void seedAdminAndClients() {

        Role adminRole = new Role(RoleEnum.ADMIN);
        Role clientRole = new Role(RoleEnum.CLIENT);

        User admin = UserFactory.createUser(User.class, "Admin", "User", "admin@admin.com", hash("admin123"));
        admin.setRoles(Stream.of(adminRole, clientRole).collect(Collectors.toSet()));

        User client1 = UserFactory.createUser(User.class, "John", "Doe", "johndoe@johndoe.com", hash("johndoe123"));
        client1.setRoles(Stream.of(clientRole).collect(Collectors.toSet()));

        User client2 = UserFactory.createUser(User.class, "Test", "User", "test@test.com", hash("test123"));
        client2.setRoles(Stream.of(clientRole).collect(Collectors.toSet()));

        User client3 = UserFactory.createUser(User.class, "Test2", "User", "test2@test.com", hash("test123"));
        client3.setRoles(Stream.of(clientRole).collect(Collectors.toSet()));

        userRepository.saveAll(Arrays.asList(admin, client1, client2, client3));

    }

    public void seedEmployeeServicesAndShifts() {
        final Room laserRoom = RoomFactory.createRoom("Laser Room");
        final Room massageRoom1 = RoomFactory.createRoom("Massage Room 1");
        final Room massageRoom2 = RoomFactory.createRoom("Massage Room 2");
        final Room pedicureRoom = RoomFactory.createRoom("Pedicure Room");
        final Room makeupRoom = RoomFactory.createRoom("Make-up Room");

        final Set<Room> massageRoomSet = Sets.newHashSet(massageRoom1, massageRoom2);
        final Set<Room> massageRoomExtraSet = Sets.newHashSet(massageRoom1, massageRoom2, makeupRoom);
        final Set<Room> anyRoom = Sets.newHashSet(laserRoom, massageRoom1, massageRoom2, pedicureRoom, makeupRoom);
        final Set<Room> laserRoomOnly = Sets.newHashSet(laserRoom);
        final Set<Room> pedicureRoomOnly = Sets.newHashSet(pedicureRoom);

        List<Service> services = new ArrayList<>();

        services.add(ServiceFactory.createService("Back to Pure Life", 150, massageRoomSet));
        services.add(ServiceFactory.createService("Quick Visit to the Spa", 100, massageRoomSet));
        services.add(ServiceFactory.createService("Business Only", 120, massageRoomExtraSet));
        services.add(ServiceFactory.createService("Mec Extra", 200, laserRoomOnly));
        services.add(ServiceFactory.createService("Get Back on Track", 170, massageRoomExtraSet));
        services.add(ServiceFactory.createService("Slender Quest", 200, massageRoomSet));
        services.add(ServiceFactory.createService("Serenity", 140, massageRoomExtraSet));
        services.add(ServiceFactory.createService("Ultimate Escape", 160, massageRoomSet));
        services.add(ServiceFactory.createService("Divine Relaxation", 150, massageRoomExtraSet));
        services.add(ServiceFactory.createService("1/2 Day Passport", 165));
        services.add(ServiceFactory.createService("The Full Day Passport", 325, anyRoom));
        services.add(ServiceFactory.createService("Poetry for Two", 180, anyRoom));
        services.add(ServiceFactory.createService("Ultimate Couples Treat", 320, massageRoomExtraSet));
        services.add(ServiceFactory.createService("Body After Baby", 120, pedicureRoomOnly));
        services.add(ServiceFactory.createService("Lost Your Soul", 120, laserRoomOnly));
        services.add(ServiceFactory.createService("Reconnect With Your Body", 210, pedicureRoomOnly));

        serviceRepository.saveAll(services);

        // Employee Role
        Role employeeRole = new Role(RoleEnum.EMPLOYEE);

        // Create services for employees
        Set<Service> set = new HashSet<>(Arrays.asList(
                services.get(0), services.get(1), services.get(3),
                services.get(6), services.get(8), services.get(11)
        ));

        // Create shifts for employees
        Set<Shift> shifts = new HashSet<>();
        shifts.add(ShiftFactory.createShift(LocalDate.of(2019, 11, 30), LocalTime.of(12, 0), LocalTime.of(21, 0)));
        shifts.add(ShiftFactory.createShift(LocalDate.of(2019, 12, 2), LocalTime.of(12, 0), LocalTime.of(21, 0)));
        shifts.add(ShiftFactory.createShift(LocalDate.of(2019, Month.FEBRUARY, 27), LocalTime.of(8, 0), LocalTime.of(22, 0)));

        Employee employee = (Employee) UserFactory.createUser(Employee.class, "Employee", "User", "employee@employee.com", hash("employee123"));
        employee.setServices(set);
        employee.setShifts(shifts);
        employee.setRoles(Sets.newHashSet(employeeRole));

        Employee employee2 = (Employee) UserFactory.createUser(Employee.class, "Employee2", "User", "employee2@employee.com", hash("employee123"));
        employee2.setServices(set);
        employee2.setShifts(shifts);
        employee2.setRoles(Sets.newHashSet(employeeRole));

        Employee employee3 = (Employee) UserFactory.createUser(Employee.class, "Employee3", "User", "employee3@employee.com", hash("employee123"));
        employee3.setServices(set);
        employee3.setShifts(shifts);
        employee3.setRoles(Sets.newHashSet(employeeRole));

        Employee employee4 = (Employee) UserFactory.createUser(Employee.class, "Employee4", "User", "employe4e@employee.com", hash("employee123"));
        employee4.setServices(set);
        employee4.setShifts(shifts);
        employee4.setRoles(Sets.newHashSet(employeeRole));

        userRepository.saveAll(Arrays.asList(employee, employee2, employee3, employee4));
    }

    public void seedAppointments() {

        List<User> clients = userRepository.findByRoles_Role(RoleEnum.CLIENT);
        List<Employee> employees = employeeRepository.findAll();
        List<Service> services = serviceRepository.findAll();

        List<Appointment> appointments = new ArrayList<>();

        appointments.add(AppointmentFactory.createAppointment(
                clients.get(0), employees.get(0), services.get(11),
                LocalDate.of(2019, 11, 30), LocalTime.of(12, 0), "Some note"
        ));

        appointments.add(AppointmentFactory.createAppointment(
                clients.get(1), employees.get(0), services.get(6),
                LocalDate.of(2019, Month.FEBRUARY, 27), LocalTime.of(12, 0), "Some note"
        ));

        appointments.add(AppointmentFactory.createAppointment(
                clients.get(2), employees.get(1), services.get(6),
                LocalDate.of(2019, Month.FEBRUARY, 27), LocalTime.of(12, 0), "Some note"
        ));

        appointments.add(AppointmentFactory.createAppointment(
                clients.get(3), employees.get(2), services.get(4),
                LocalDate.of(2019, Month.FEBRUARY, 27), LocalTime.of(12, 0), "Some note"
        ));

        appointmentRepository.saveAll(appointments);

    }

    public String hash(String password) {
        return bCryptPasswordEncoder.encode(password);
    }
}
