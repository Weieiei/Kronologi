package appointmentscheduler.controller.rest;

import appointmentscheduler.annotation.LogREST;
import appointmentscheduler.converters.service.ServiceDTOToService;
import appointmentscheduler.dto.employee.EmployeeShiftDTO;
import appointmentscheduler.dto.service.ServiceCreateDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.employee_service.EmployeeService;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.repository.EmployeeServiceRepository;
import appointmentscheduler.serializer.*;
import appointmentscheduler.service.appointment.AppointmentService;
import appointmentscheduler.service.business.BusinessService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "${rest.api.path}/business", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController extends AbstractController {


    // to be redone with serializer
    @Autowired
    private ServiceDTOToService serviceConverter;

    private AppointmentService appointmentService;
    private UserService userService;
    private ServiceService serviceService;
    private BusinessService businessService;
    private EmployeeServiceRepository employeeServiceRepository;

    private EmployeeShiftService employeeShiftService;
    private ObjectMapperFactory objectMapperFactory;

    @Autowired
    public AdminController(BusinessService businessService, EmployeeServiceRepository employeeServiceRepository,
                           AppointmentService appointmentService,
                           UserService userService,
                           ServiceService serviceService, EmployeeShiftService employeeShiftService,
                           ObjectMapperFactory objectMapperFactory) {
        this.businessService = businessService;
        this.employeeServiceRepository = employeeServiceRepository;
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.serviceService = serviceService;
        this.employeeShiftService = employeeShiftService;
        this.objectMapperFactory = objectMapperFactory;
    }

    @LogREST
    @GetMapping("/{businessId}/admin/employees")
    public ResponseEntity<String> getEmployees(@PathVariable long businessId) {
        Set<Employee> employees = employeeShiftService.getEmployeesForBusiness(businessId);
        final ObjectMapper mapper = objectMapperFactory.createMapper(Employee.class, new AdminEmployeeSerializer());
        return getJson(mapper, employees);
    }

    @GetMapping("/{businessId}/admin/employee/{employeeId}")
    public ResponseEntity<String> getEmployee(@PathVariable long businessId,
                                              @PathVariable long employeeId) {
        Employee employee = employeeShiftService.getEmployeeByBusinessId(employeeId, businessId);
        final ObjectMapper mapper = objectMapperFactory.createMapper(Employee.class, new AdminEmployeeSerializer());
        return getJson(mapper, employee);
    }


    @GetMapping("/{businessId}/admin/employee/{employeeId}/shift")
    public ResponseEntity<String> getEmployeeShifts(@PathVariable long businessId,
                                                    @PathVariable long employeeId) {
        List<Shift> shifts = employeeShiftService.getEmployeeShiftsForBusiness(employeeId, businessId);
        final ObjectMapper mapper = objectMapperFactory.createMapper(Shift.class, new AdminEmployeeShiftSerializer());
        return getJson(mapper, shifts);
    }

    @LogREST
    @PostMapping("/{businessId}/admin/employee/{employeeId}/shift")
    public ResponseEntity<String> createShift(@PathVariable long employeeId, @PathVariable long businessId,
                                              @RequestBody EmployeeShiftDTO employeeShiftDTO) {
        Shift shift = employeeShiftService.createShiftForBusiness(employeeId, businessId, employeeShiftDTO);
        final ObjectMapper mapper = objectMapperFactory.createMapper(Shift.class, new AdminEmployeeShiftSerializer());
        return getJson(mapper, shift);
    }

    @LogREST
    @PostMapping("/{businessId}/admin/employee/{employeeId}/shift-list")
    public ResponseEntity<String> createListShift(@PathVariable long employeeId, @PathVariable long businessId,
                                              @RequestBody List<EmployeeShiftDTO> employeeShiftDTO) {
        List<Shift> shift = employeeShiftService.addShiftList(employeeId, businessId, employeeShiftDTO);
        final ObjectMapper mapper = objectMapperFactory.createMapper(Shift.class, new AdminEmployeeShiftSerializer());
        return getJson(mapper, shift);
    }

    @LogREST
    @PutMapping("/{businessId}/admin/employee/{employeeId}/shift/{shiftId}")
    public ResponseEntity<String> modifyShift(@PathVariable long businessId, @PathVariable long employeeId,
                                              @PathVariable long shiftId,
                                              @RequestBody EmployeeShiftDTO employeeShiftDTO) {
        Shift shift = employeeShiftService.modifyShift(businessId, employeeId, employeeShiftDTO, shiftId);
        final ObjectMapper mapper = objectMapperFactory.createMapper(Shift.class, new AdminEmployeeShiftSerializer());
        return getJson(mapper, shift);
    }

    @LogREST
    @DeleteMapping("/{businessId}/admin/employee/shift/{shiftId}")
    public void deleteShift(@PathVariable long businessId, @PathVariable long shiftId){
        employeeShiftService.deleteShift(shiftId, businessId);
    }

    @LogREST
    @GetMapping("/{businessId}/admin/client/{clientId}/appointments")
    public ResponseEntity<String> getAllAppointments(@PathVariable long businessId, @PathVariable long clientId) {
        ObjectMapper mapper = objectMapperFactory.createMapper(Appointment.class, new UserAppointmentSerializer());
        return getJson(mapper, appointmentService.findByClientIdAndBusinessId(clientId, businessId));
    }

    @GetMapping("/{businessId}/admin/employee/{employeeId}/appointments")
    public ResponseEntity<String> employeeAppointmentList(@PathVariable long businessId, @PathVariable long employeeId) {
        ObjectMapper mapper = objectMapperFactory.createMapper(Appointment.class, new UserAppointmentSerializer());
        return getJson(mapper, appointmentService.findByEmployeeIdAndBusinessId(employeeId, businessId));
    }

    @GetMapping("/{businessId}/admin/users")
    public ResponseEntity<String> getAllUsers(@PathVariable long businessId) {
        ObjectMapper mapper = objectMapperFactory.createMapper(User.class, new UserSerializer());
        return getJson(mapper, userService.findAllUsersForBusiness(businessId, RoleEnum.CLIENT));
    }

    @GetMapping("/{businessId}/admin/clients")
    public ResponseEntity<String> getAllClients() {
        ObjectMapper mapper = objectMapperFactory.createMapper(User.class, new UserSerializer());
        return getJson(mapper, userService.findAllClients());
    }

    @LogREST
    @GetMapping("/{businessId}/admin/appointments")
    public ResponseEntity<String> getAllAppointments(@PathVariable long businessId){
        ObjectMapper mapper = objectMapperFactory.createMapper(Appointment.class, new UserAppointmentSerializer());
        return getJson(mapper, appointmentService.findByBusinessId(businessId));
    }

    @PostMapping("/{businessId}/admin/user/employee/{id}")
    public ResponseEntity<Map<String, String>> changeRoleToEmployee(@PathVariable long businessId,
                                                                    @PathVariable long id){
        User user = this.userService.findUserById(id);
        RoleEnum role = RoleEnum.valueOf(user.getRole());
        if (role.toString().equals(RoleEnum.EMPLOYEE.toString())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        return ResponseEntity.ok(userService.updateUser(user, businessId));
    }

    //TODO make sure that an employee doesnt have the given service already assigned to them before storing it in the db
    @PostMapping("/{businessId}/admin/service/{employeeId}/{serviceId}")
    public ResponseEntity<Map<String, String>> assignService(@PathVariable long businessId,
                                                             @PathVariable long employeeId, @PathVariable long serviceId){
        Employee employee = employeeShiftService.getEmployeeByBusinessId(employeeId, businessId);
        Service service = serviceService.findByIdAndBusinessId(serviceId, businessId);
        Business business = businessService.findById(businessId);
        employee.addService(service);
        EmployeeService employee_service = new EmployeeService(business, employee, service);
        employeeServiceRepository.save(employee_service);
        return ResponseEntity.ok(message("service assigned"));
    }

    //todo make sure you arent adding duplicate services
    @LogREST
    @PostMapping("/{businessId}/admin/service")
    public ResponseEntity<Map<String, String>> add(@PathVariable long businessId,
                                                   @RequestBody ServiceCreateDTO serviceCreateDTO) {
        Service service = serviceConverter.convert(serviceCreateDTO);
        Business business = businessService.findById(businessId);
        service.setBusiness(business);
        return ResponseEntity.ok(serviceService.add(service));
    }
    private Map<String, String> message(String message) {
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        return map;
    }
}