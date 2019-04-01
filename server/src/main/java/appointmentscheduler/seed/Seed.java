package appointmentscheduler.seed;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.appointment.AppointmentFactory;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.employee_service.EmployeeService;
import appointmentscheduler.entity.event.AppEventBase;
import appointmentscheduler.entity.phonenumber.PhoneNumber;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.service.ServiceFactory;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.entity.user.UserFactory;
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
import java.time.Month;
import java.util.*;

@Component
public class Seed {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeServiceRepository employeeServiceRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private VerificationRepository verificationRepository;


    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    Business business;

    Business business2;

    @EventListener
    public void seed(ContextRefreshedEvent event) throws NoSuchAlgorithmException {
         business = new Business("my business", "beauty", "my description");
         businessRepository.save(business);
        business2 = new Business("2", "2", "2");
        businessRepository.save(business2);

      //  boolean noAdmin = userRepository.findByRoles_Role(RoleEnum.ADMIN).isEmpty();
        boolean noAdmin = true;
        if (noAdmin) {
            seedAdminAndClientsAndPhoneNumbers();
            seedEmployeeServicesAndShifts();
        }

    }

    public void seedAdminAndClientsAndPhoneNumbers() throws NoSuchAlgorithmException{


        User admin = UserFactory.createUser(business, User.class, "Admin", "User", "admin@admin.com", hash("admin123"));
        admin.setBusiness(business);
        admin.setVerified(true);
        admin.setRole(RoleEnum.ADMIN.toString());
        User adminCopy = UserFactory.createUser(business, User.class, "Admin", "User", "admin@admin.com", hash(
                "admin123"));
        adminCopy.setVerified(true);

        adminCopy.setRole(RoleEnum.CLIENT.toString());

        User client1 = UserFactory.createUser(business, User.class, "John", "Doe", "johndoe@johndoe.com", hash(
                "johndoe123"));
        client1.setRole(RoleEnum.CLIENT.toString());

        User client1Copy = UserFactory.createUser(business, User.class, "John", "Doe", "johndoe@johndoe.com", hash(
                "johndoe123"));
        client1Copy.setRole(RoleEnum.CLIENT.toString());

        User client2 = UserFactory.createUser(business, User.class, "Test", "User", "test@test.com", hash("test123"));
        client2.setVerified(true);
        client2.setRole(RoleEnum.CLIENT.toString());
        //client2.setRoles(Stream.of(clientRole).collect(Collectors.toSet()));

        User client3 = UserFactory.createUser(business, User.class, "Test2", "User", "test2@test.com", hash("test123"));
//        client3.setRoles(Stream.of(clientRole).collect(Collectors.toSet()));
        client3.setRole(RoleEnum.CLIENT.toString());

        Verification verifyUser1 = new Verification(client1);
        Verification verifyUser2 = new Verification(client2);

        admin.setPhoneNumber(new PhoneNumber("1", "514", "5551234", admin));
        client1.setPhoneNumber(new PhoneNumber("1", "514", "5552345", client1));
        client2.setPhoneNumber(new PhoneNumber("1", "514", "5553456", client2));

        userRepository.saveAll(Arrays.asList(admin, client1, client2, client3));
        verificationRepository.saveAll(Arrays.asList(verifyUser1, verifyUser2));

    }

    public void seedEmployeeServicesAndShifts() {

        List<Service> services = new ArrayList<>();

        services.add(ServiceFactory.createService(business, "Back to Pure Life", 150));
        services.add(ServiceFactory.createService(business, "Quick Visit to the Spa", 100));
        services.add(ServiceFactory.createService(business, "Business Only", 120));
        services.add(ServiceFactory.createService(business,"Mec Extra", 200));
        services.add(ServiceFactory.createService(business,"Get Back on Track", 170));
        services.add(ServiceFactory.createService(business,"Slender Quest", 200));
        services.add(ServiceFactory.createService(business,"Serenity", 140));
        services.add(ServiceFactory.createService(business,"Ultimate Escape", 160));
        services.add(ServiceFactory.createService(business2,"Divine Relaxation", 150));
        services.add(ServiceFactory.createService(business2,"1/2 Day Passport", 165));
        services.add(ServiceFactory.createService(business2,"The Full Day Passport", 325));
        services.add(ServiceFactory.createService(business2,"Poetry for Two", 180));
        services.add(ServiceFactory.createService(business2,"Ultimate Couples Treat", 320));
        services.add(ServiceFactory.createService(business2,"Body After Baby", 120));
        services.add(ServiceFactory.createService(business2,"Lost Your Soul", 120));
        services.add(ServiceFactory.createService(business2,"Reconnect With Your Body", 210));

        serviceRepository.saveAll(services);

        // Create services for employees

        Set<Service> set = new HashSet<>(Arrays.asList(
                services.get(0), services.get(1), services.get(3),
                services.get(6), services.get(8), services.get(11)
        ));

        Employee employee = (Employee) UserFactory.createUser(business, Employee.class, "Employee", "User", "employee" +
                "@employee.com", hash("employee123"));
        Employee employee2 = (Employee) UserFactory.createUser(business, Employee.class, "Employee2", "User",
                "employee2@employee.com", hash("employee123"));
        Employee employee3 = (Employee) UserFactory.createUser(business2, Employee.class, "Employee3", "User",
                "employee3@employee.com", hash("employee123"));
        Employee employee4 = (Employee) UserFactory.createUser(business2, Employee.class, "Employee4", "User",
                "employe4e@employee.com", hash("employee123"));

        employeeRepository.saveAll(Arrays.asList(employee, employee2, employee3, employee4));

        employee.addEmployeeService(services.get(6));
        employee.addEmployeeService(services.get(8));
        employee.setVerified(true);
        employee.setShifts(generateShifts(employee, employee.getBusiness()));
//        employee.setRoles(Sets.newHashSet(employeeRole));
        employee.setRole(RoleEnum.EMPLOYEE.toString());
        employee.setPhoneNumber(new PhoneNumber("1", "514", "5554567", employee));


        employee2.addEmployeeService(services.get(3));
        employee2.addEmployeeService(services.get(11));
        employee2.setVerified(true);
        employee2.setShifts(generateShifts(employee2, employee.getBusiness()));
//        employee2.setRoles(Sets.newHashSet(employeeRole));
        employee2.setRole(RoleEnum.EMPLOYEE.toString());


        employee3.addEmployeeService(services.get(0));
        employee3.addEmployeeService(services.get(11));
        employee3.setVerified(true);
        employee3.setShifts(generateShifts(employee3, employee.getBusiness()));
//        employee3.setRoles(Sets.newHashSet(employeeRole));
        employee3.setRole(RoleEnum.EMPLOYEE.toString());


        employee4.addEmployeeService(services.get(3));
        employee4.addEmployeeService(services.get(6));
        employee4.setVerified(true);
        employee4.setShifts(generateShifts(employee4, employee.getBusiness()));
//        employee4.setRoles(Sets.newHashSet(employeeRole));
        employee4.setRole(RoleEnum.EMPLOYEE.toString());



        employee.createService(services.get(0), business);
        employee.createService(services.get(1), business);
        employee2.createService(services.get(2), business);
        employee2.createService(services.get(3), business);
        employee2.createService(services.get(4), business);
        employee3.createService(services.get(6), business);
        employee3.createService(services.get(6), business);
        employee4.createService(services.get(6), business);
        employee4.createService(services.get(6), business);

        Set<EmployeeService> service1 = employee.getServices();
        Set<EmployeeService> service2 = employee2.getServices();
        Set<EmployeeService> service3 = employee3.getServices();
        Set<EmployeeService> service4 = employee4.getServices();

        Set<EmployeeService> totalSet = new HashSet<>();
        totalSet.addAll(service1);
        totalSet.addAll(service2);
        totalSet.addAll(service3);
        totalSet.addAll(service4);
        employeeServiceRepository.saveAll(totalSet);

    }

    public void seedAppointments(Shift shift) {

        List<User> clients = userRepository.findByRole(RoleEnum.CLIENT);
        Employee employee;
        Service service1;
        Service service2;

        List<Appointment> appointments = new ArrayList<>();

        employee = shift.getEmployee();
        service1 = employee.getEmployeeServices().get(0);
        service2 = employee.getEmployeeServices().get(1);

        appointments.add(AppointmentFactory.createAppointment(
                business, clients.get(0), employee, service1,
                shift.getDate(), shift.getStartTime(), "Some note"
        ));

        appointments.add(AppointmentFactory.createAppointment(
                business, clients.get(1), employee, service2,
                shift.getDate(), shift.getEndTime().minusMinutes(service2.getDuration()), "Some note"
        ));

        appointmentRepository.saveAll(appointments);



    }

    private String hash(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    private Set<Shift> generateShifts(Employee employee, Business business) {
        // Create shifts for employee
        Shift shift0 = new Shift(business, employee, LocalDate.now().minusDays(1), LocalTime.of(12, 0), LocalTime.of(22, 0));
        Shift shift1 = new Shift(business, employee, LocalDate.now().plusDays(1), LocalTime.of(12, 0), LocalTime.of(22, 0));
        Shift shift2 = new Shift(business, employee, LocalDate.now().plusDays(2), LocalTime.of(12, 0), LocalTime.of(21, 0));
        Shift shift3 = new Shift(business, employee, LocalDate.now().plusDays(3), LocalTime.of(8, 0), LocalTime.of(22, 0));
        Set<Shift> shifts = new HashSet<>();

        //past appointment
        shifts.add(shift0);
        shifts.add(shift1);
        shifts.add(shift2);
        shifts.add(shift3);

        shiftRepository.saveAll(shifts);

        seedAppointments(shift0);
        seedAppointments(shift1);
        seedAppointments(shift2);
        seedAppointments(shift3);

        return shifts;
    }
}
