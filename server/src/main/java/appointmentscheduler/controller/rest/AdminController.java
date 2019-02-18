package appointmentscheduler.controller.rest;

import appointmentscheduler.dto.employee.EmployeeShiftDTO;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.serializer.AdminEmployeeSerializer;
import appointmentscheduler.serializer.AdminEmployeeShiftSerializer;
import appointmentscheduler.serializer.ObjectMapperFactory;
import appointmentscheduler.service.employee.EmployeeShiftService;
import com.fasterxml.jackson.databind.ObjectMapper;
import appointmentscheduler.dto.service.ServiceCreateDTO;
import appointmentscheduler.converters.service.ServiceDTOToService;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.role.Role;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.repository.RoleRepository;
import appointmentscheduler.repository.ServiceRepository;
import appointmentscheduler.service.appointment.AppointmentService;
import appointmentscheduler.service.service.ServiceService;
import appointmentscheduler.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import appointmentscheduler.dto.employee.EmployeeShiftDTO;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.service.employee.EmployeeShiftService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping(value = "${rest.api.path}/admin", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController extends AbstractController {


    // to be redone with serializer
    @Autowired
    private ServiceDTOToService serviceConverter;

    private AppointmentService appointmentService;
    private UserService userService;
    private RoleRepository roleRepository;
    private ServiceService serviceService;
    private ServiceRepository serviceRepository;

    private EmployeeShiftService employeeShiftService;
    private ObjectMapperFactory objectMapperFactory;

    //from ema's branch
    @Autowired
    public AdminController( AppointmentService appointmentService, UserService userService, ServiceService serviceService,
                            RoleRepository roleRepository, ServiceRepository serviceRepository, EmployeeShiftService employeeShiftService,
                            ObjectMapperFactory objectMapperFactory) {
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.serviceService = serviceService;
        this.roleRepository = roleRepository;
        this.serviceRepository = serviceRepository;
        this.employeeShiftService = employeeShiftService;
        this.objectMapperFactory = objectMapperFactory;
    }

    @GetMapping("/employee")
    public ResponseEntity<String> getEmployees() {
        List<Employee> employees = employeeShiftService.getEmployees();
        final ObjectMapper mapper = objectMapperFactory.createMapper(Employee.class, new AdminEmployeeSerializer());
        return getJson(mapper, employees);
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<String> getEmployees(@PathVariable long id) {
        Employee employee = employeeShiftService.getEmployee(id);
        final ObjectMapper mapper = objectMapperFactory.createMapper(Employee.class, new AdminEmployeeSerializer());
        return getJson(mapper, employee);
    }

    @GetMapping("/employee/{employeeId}/shift")
    public ResponseEntity<String> getEmployeeShifts(@PathVariable long employeeId) {
        List<Shift> shifts = employeeShiftService.getEmployeeShifts(employeeId);
        final ObjectMapper mapper = objectMapperFactory.createMapper(Shift.class, new AdminEmployeeShiftSerializer());
        return getJson(mapper, shifts);
    }

    @PostMapping("/employee/{employeeId}/shift")
    public ResponseEntity<String> createShift(@PathVariable long employeeId, @RequestBody EmployeeShiftDTO employeeShiftDTO) {
        Shift shift = employeeShiftService.createShift(employeeId, employeeShiftDTO);
        final ObjectMapper mapper = objectMapperFactory.createMapper(Shift.class, new AdminEmployeeShiftSerializer());
        return getJson(mapper, shift);
    }

    @PutMapping("/employee/{employeeId}/shift/{shiftId}")
    public ResponseEntity<String> modifyShift(@PathVariable long employeeId, @PathVariable long shiftId, @RequestBody EmployeeShiftDTO employeeShiftDTO) {
        Shift shift = employeeShiftService.modifyShift(employeeId, employeeShiftDTO, shiftId);
        final ObjectMapper mapper = objectMapperFactory.createMapper(Shift.class, new AdminEmployeeShiftSerializer());
        return getJson(mapper, shift);
    }

    @DeleteMapping("/employee/shift/{shiftId}")
    public void deleteShift(@PathVariable long shiftId){
        employeeShiftService.deleteShift(shiftId);
    }

    @GetMapping("/client/{id}")
    public List<Appointment> clientAppointmentList(@PathVariable long clientId){
        return this.appointmentService.findByClientId(clientId);
    }

    @GetMapping("/employee/appointments/{id}")
    public List<Appointment> employeeAppointmentList(@PathVariable long employeeId) {
        return this.appointmentService.findByEmployeeId(employeeId);
    }

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.findAll();
    }

    @GetMapping("/appointments")
    public List<Appointment> getAllAppointments(){
        return appointmentService.findAll();
    }

//todo refactor to use existing code
    @PostMapping("/user/employee/{id}")
    public ResponseEntity<Map<String, Object>> changeRoleToEmployee(@PathVariable long id){
        User user = this.userService.findUserByid(id);
        Set<Role> roles = user.getRoles();
        for (Role role: roles) {
            if (role.getRole() == RoleEnum.EMPLOYEE) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }
        user.addRoles(this.roleRepository.findByRole(RoleEnum.EMPLOYEE));
        if (userService.updateUser(user))
            return ResponseEntity.status(HttpStatus.OK).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    private boolean containAnyRole(Set<Role> roles, RoleEnum roleType) {
        return roles.stream().anyMatch(role -> role.getRole() == roleType);
    }

    // for assigning services to employees (employees can perform certain services)
    @PostMapping("service/{employeeId}/{serviceId}")
    public ResponseEntity<Map<String, Object>> assignService(@PathVariable long employeeId, @PathVariable long serviceId){
        User user = this.userService.findUserByid(employeeId);
        Set<Role> roles = user.getRoles();
        //check if user is an employee

        if (containAnyRole(roles, RoleEnum.EMPLOYEE)) {
            //check if the employee can already perform the service
            if (user.getEmployeeServices().contains(serviceRepository.findById(serviceId))){
                System.out.println("The employee has already been assigned that service");
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            else {
                // assign the service
                Optional<Service> optionalService = serviceRepository.findById(serviceId);
                if (optionalService.isPresent()) {
                    user.addEmployeeService(optionalService.get());
                    if (userService.updateUser(user))
                        return ResponseEntity.status(HttpStatus.OK).build();
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
                else {
                    System.out.println("The ID provided was not a valid Service ID.");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
            }
        }
        else {
            System.out.println("The user is not an employee, and therefore cannot be assigned a service");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // for adding a new service
    @PostMapping("service")
    public Service addService(@RequestBody ServiceCreateDTO serviceDTO){
        Service service = serviceConverter.convert(serviceDTO);
        return serviceService.add(service);
    }


}
