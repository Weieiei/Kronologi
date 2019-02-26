//package appointmentscheduler.seed;
//
//import appointmentscheduler.entity.appointment.Appointment;
//import appointmentscheduler.entity.appointment.AppointmentFactory;
//import appointmentscheduler.entity.business.Business;
//import appointmentscheduler.entity.phonenumber.PhoneNumber;
//import appointmentscheduler.entity.role.Role;
//import appointmentscheduler.entity.role.RoleEnum;
//import appointmentscheduler.entity.room.Room;
//import appointmentscheduler.entity.room.RoomFactory;
//import appointmentscheduler.entity.service.Service;
//import appointmentscheduler.entity.service.ServiceFactory;
//import appointmentscheduler.entity.shift.Shift;
//import appointmentscheduler.entity.shift.ShiftFactory;
//import appointmentscheduler.entity.user.Employee;
//import appointmentscheduler.entity.user.User;
//import appointmentscheduler.entity.verification.Verification;
//import appointmentscheduler.entity.user.UserFactory;
//import appointmentscheduler.repository.*;
//import com.google.common.collect.Sets;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.security.NoSuchAlgorithmException;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.time.Month;
//import java.util.*;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//@Component
//public class Seed {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private EmployeeRepository employeeRepository;
//
//    @Autowired
//    private ServiceRepository serviceRepository;
//
//    @Autowired
//    private AppointmentRepository appointmentRepository;
//
//    @Autowired
//    private ShiftRepository shiftRepository;
//
//    @Autowired
//    private VerificationRepository verificationRepository;
//
//    @Autowired
//    private BusinessRepository businessRepository;
//
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    Business business;
//
//    @EventListener
//    public void seed(ContextRefreshedEvent event) throws NoSuchAlgorithmException {
//         business = new Business("my business", "beauty", "my description");
//         businessRepository.save(business);
//
//        boolean noAdmin = userRepository.findByRoles_Role(RoleEnum.ADMIN).isEmpty();
//
//        if (noAdmin) {
//            seedAdminAndClientsAndPhoneNumbers();
//            seedEmployeeServicesAndShifts();
//            seedAppointments();
//        }
//
//    }
//
//    public void seedAdminAndClientsAndPhoneNumbers() throws NoSuchAlgorithmException{
//
//        Role adminRole = new Role(RoleEnum.ADMIN);
//        Role clientRole = new Role(RoleEnum.CLIENT);
//
//        User admin = UserFactory.createUser(business, User.class, "Admin", "User", "admin@admin.com", hash("admin123"));
//        admin.setVerified(true);
//        admin.setRoles(Stream.of(adminRole, clientRole).collect(Collectors.toSet()));
//
//        User client1 = UserFactory.createUser(business, User.class, "John", "Doe", "johndoe@johndoe.com", hash(
//                "johndoe123"));
//        client1.setRoles(Stream.of(clientRole).collect(Collectors.toSet()));
//
//        User client2 = UserFactory.createUser(business, User.class, "Test", "User", "test@test.com", hash("test123"));
//        client2.setVerified(true);
//        client2.setRoles(Stream.of(clientRole).collect(Collectors.toSet()));
//
//        User client3 = UserFactory.createUser(business, User.class, "Test2", "User", "test2@test.com", hash("test123"));
//        client3.setRoles(Stream.of(clientRole).collect(Collectors.toSet()));
//
//        Verification verifyUser1 = new Verification(client1);
//        Verification verifyUser2 = new Verification(client2);
//
//        admin.setPhoneNumber(new PhoneNumber("1", "514", "5551234", admin));
//        client1.setPhoneNumber(new PhoneNumber("1", "514", "5552345", client1));
//        client2.setPhoneNumber(new PhoneNumber("1", "514", "5553456", client2));
//
//        userRepository.saveAll(Arrays.asList(admin, client1, client2, client3));
//        verificationRepository.saveAll(Arrays.asList(verifyUser1, verifyUser2));
//
//    }
//
//    public void seedEmployeeServicesAndShifts() {
//        final Room laserRoom = RoomFactory.createRoom("Laser Room");
//        final Room massageRoom1 = RoomFactory.createRoom("Massage Room 1");
//        final Room massageRoom2 = RoomFactory.createRoom("Massage Room 2");
//        final Room pedicureRoom = RoomFactory.createRoom("Pedicure Room");
//        final Room makeupRoom = RoomFactory.createRoom("Make-up Room");
//
//        final Set<Room> massageRoomSet = Sets.newHashSet(massageRoom1, massageRoom2);
//        final Set<Room> massageRoomExtraSet = Sets.newHashSet(massageRoom1, massageRoom2, makeupRoom);
//        final Set<Room> anyRoom = Sets.newHashSet(laserRoom, massageRoom1, massageRoom2, pedicureRoom, makeupRoom);
//        final Set<Room> laserRoomOnly = Sets.newHashSet(laserRoom);
//        final Set<Room> pedicureRoomOnly = Sets.newHashSet(pedicureRoom);
//
//        List<Service> services = new ArrayList<>();
//
//        services.add(ServiceFactory.createService(business, "Back to Pure Life", 150, massageRoomSet));
//        services.add(ServiceFactory.createService(business, "Quick Visit to the Spa", 100, massageRoomSet));
//        services.add(ServiceFactory.createService(business, "Business Only", 120, massageRoomExtraSet));
//        services.add(ServiceFactory.createService(business,"Mec Extra", 200, laserRoomOnly));
//        services.add(ServiceFactory.createService(business,"Get Back on Track", 170, massageRoomExtraSet));
//        services.add(ServiceFactory.createService(business,"Slender Quest", 200, massageRoomSet));
//        services.add(ServiceFactory.createService(business,"Serenity", 140, massageRoomExtraSet));
//        services.add(ServiceFactory.createService(business,"Ultimate Escape", 160, massageRoomSet));
//        services.add(ServiceFactory.createService(business,"Divine Relaxation", 150, massageRoomExtraSet));
//        services.add(ServiceFactory.createService(business,"1/2 Day Passport", 165));
//        services.add(ServiceFactory.createService(business,"The Full Day Passport", 325, anyRoom));
//        services.add(ServiceFactory.createService(business,"Poetry for Two", 180, anyRoom));
//        services.add(ServiceFactory.createService(business,"Ultimate Couples Treat", 320, massageRoomExtraSet));
//        services.add(ServiceFactory.createService(business,"Body After Baby", 120, pedicureRoomOnly));
//        services.add(ServiceFactory.createService(business,"Lost Your Soul", 120, laserRoomOnly));
//        services.add(ServiceFactory.createService(business,"Reconnect With Your Body", 210, pedicureRoomOnly));
//
//        serviceRepository.saveAll(services);
//
//        // Employee Role
//        Role employeeRole = new Role(RoleEnum.EMPLOYEE);
//
//
//        // Create services for employees
//        Set<Service> set = new HashSet<>(Arrays.asList(
//                services.get(0), services.get(1), services.get(3),
//                services.get(6), services.get(8), services.get(11)
//        ));
//
//        Employee employee = (Employee) UserFactory.createUser(business, Employee.class, "Employee", "User", "employee" +
//                "@employee.com", hash("employee123"));
//        employee.setServices(set);
//        employee.setVerified(true);
//        employee.setShifts(generateShifts(employee));
//        employee.setRoles(Sets.newHashSet(employeeRole));
//        employee.setPhoneNumber(new PhoneNumber("1", "514", "5554567", employee));
//
//        Employee employee2 = (Employee) UserFactory.createUser(business, Employee.class, "Employee2", "User",
//                "employee2@employee.com", hash("employee123"));
//        employee2.setServices(set);
//        employee2.setVerified(true);
//        employee2.setShifts(generateShifts(employee2));
//        employee2.setRoles(Sets.newHashSet(employeeRole));
//
//        Employee employee3 = (Employee) UserFactory.createUser(business, Employee.class, "Employee3", "User",
//                "employee3@employee.com", hash("employee123"));
//        employee3.setServices(set);
//        employee3.setVerified(true);
//        employee3.setShifts(generateShifts(employee3));
//        employee3.setRoles(Sets.newHashSet(employeeRole));
//
//        Employee employee4 = (Employee) UserFactory.createUser(business, Employee.class, "Employee4", "User",
//                "employe4e@employee.com", hash("employee123"));
//        employee4.setServices(set);
//        employee4.setVerified(true);
//        employee4.setShifts(generateShifts(employee4));
//        employee4.setRoles(Sets.newHashSet(employeeRole));
//
//        employeeRepository.saveAll(Arrays.asList(employee, employee2, employee3, employee4));
//    }
//
//    public void seedAppointments() {
//
//        List<User> clients = userRepository.findByRoles_Role(RoleEnum.CLIENT);
//        List<Employee> employees = employeeRepository.findAll();
//        List<Service> services = serviceRepository.findAll();
//
//        List<Appointment> appointments = new ArrayList<>();
//
//        appointments.add(AppointmentFactory.createAppointment(
//                business, clients.get(0), employees.get(0), services.get(11),
//                LocalDate.of(2019, Month.FEBRUARY, 27), LocalTime.of(12, 0), "Some note"
//        ));
//
//        appointments.add(AppointmentFactory.createAppointment(
//                business, clients.get(1), employees.get(0), services.get(6),
//                LocalDate.of(2019, Month.FEBRUARY, 27), LocalTime.of(16, 0), "Some note"
//        ));
//
//        appointments.add(AppointmentFactory.createAppointment(
//                business, clients.get(2), employees.get(1), services.get(6),
//                LocalDate.of(2019, Month.MARCH, 6), LocalTime.of(12, 0), "Some note"
//        ));
//
//        appointments.add(AppointmentFactory.createAppointment(
//                business, clients.get(3), employees.get(2), services.get(4),
//                LocalDate.of(2019, Month.MARCH, 30), LocalTime.of(12, 0), "Some note"
//        ));
//
//        //past appointments
//        appointments.add(AppointmentFactory.createAppointment(
//                business, clients.get(1), employees.get(1), services.get(6),
//                LocalDate.of(2018, Month.MARCH, 6), LocalTime.of(12, 0), "Some note"
//        ));
//
//        appointments.add(AppointmentFactory.createAppointment(
//                business, clients.get(2), employees.get(1), services.get(6),
//                LocalDate.of(2018, Month.MARCH, 6), LocalTime.of(12, 0), "Some note"
//        ));
//
//        appointments.add(AppointmentFactory.createAppointment(
//                business, clients.get(3), employees.get(2), services.get(4),
//                LocalDate.of(2018, Month.MARCH, 30), LocalTime.of(12, 0), "Some note"
//        ));
//
//        appointmentRepository.saveAll(appointments);
//
//    }
//
//    private String hash(String password) {
//        return bCryptPasswordEncoder.encode(password);
//    }
//
//    private Set<Shift> generateShifts(Employee employee) {
//        // Create shifts for employee
//        Set<Shift> shifts = new HashSet<>();
//        shifts.add(ShiftFactory.createShift(business, employee, LocalDate.of(2019, Month.FEBRUARY, 27), LocalTime.of(12,
//                0), LocalTime.of(22, 0)));
//        shifts.add(ShiftFactory.createShift(business, employee, LocalDate.of(2019, Month.MARCH, 6), LocalTime.of(12, 0),
//                LocalTime.of(21, 0)));
//        shifts.add(ShiftFactory.createShift(business, employee, LocalDate.of(2019, Month.MARCH, 30), LocalTime.of(8, 0),
//                LocalTime.of(22, 0)));
//        return shifts;
//    }
//}
