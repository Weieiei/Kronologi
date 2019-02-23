package appointmentscheduler.controller.rest;

import appointmentscheduler.annotation.LogREST;
import appointmentscheduler.converters.service.ServiceDTOToService;
import appointmentscheduler.dto.employee.EmployeeShiftDTO;
import appointmentscheduler.dto.service.ServiceCreateDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.role.Role;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.repository.RoleRepository;
import appointmentscheduler.repository.ServiceRepository;
import appointmentscheduler.serializer.*;
import appointmentscheduler.service.appointment.AppointmentService;
import appointmentscheduler.service.employee.EmployeeShiftService;
import appointmentscheduler.service.service.ServiceService;
import appointmentscheduler.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @LogREST
    @GetMapping("/users")
    public ResponseEntity<String> getAllUsers(){
        ObjectMapper mapper = objectMapperFactory.createMapper(User.class, new UserSerializer());
        return getJson(mapper, userService.findAll());
    }

    @GetMapping("/appointments")
    public ResponseEntity<String> getAllAppointments(){
        ObjectMapper mapper = objectMapperFactory.createMapper(Appointment.class, new UserAppointmentSerializer());
        return getJson(mapper, appointmentService.findAll());
    }

//todo refactor to use existing code
    @PostMapping("/user/employee/{id}")
    public ResponseEntity<Map<String, String>> changeRoleToEmployee(@PathVariable long id){
        User user = this.userService.findUserByid(id);
        Set<Role> roles = user.getRoles();
        for (Role role: roles) {
            if (role.getRole() == RoleEnum.EMPLOYEE) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }
        user.addRoles(this.roleRepository.findByRole(RoleEnum.EMPLOYEE));
        return ResponseEntity.ok(userService.updateUser(user));
    }

    private boolean containAnyRole(Set<Role> roles, RoleEnum roleType) {
        return roles.stream().anyMatch(role -> role.getRole() == roleType);
    }

    // for assigning services to employees (employees can perform certain services)
    @PostMapping("service/{employeeId}/{serviceId}")
    public ResponseEntity<Map<String, String>> assignService(@PathVariable long employeeId, @PathVariable long serviceId){
        User user = this.userService.findUserByid(employeeId);
        Set<Role> roles = user.getRoles();
        //check if user is an employee
        return serviceRepository.findById(serviceId).map(service -> {
            user.addEmployeeService(service);
            return ResponseEntity.ok(message("service assigned"));
        }).orElseThrow(() -> new ResourceNotFoundException(String.format("Service id %d not found.", serviceId)));
    }

    // for adding a new service
    @LogREST
    @PostMapping("service")
    public ResponseEntity<Map<String, String>> add(@RequestBody ServiceCreateDTO serviceCreateDTO){
        Service service = serviceConverter.convert(serviceCreateDTO);
        return ResponseEntity.ok(serviceService.add(service));
    }

    private Map<String, String> message(String message) {
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        return map;
    }

}
