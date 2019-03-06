package appointmentscheduler.controller.rest;

import appointmentscheduler.annotation.LogREST;
import appointmentscheduler.converters.service.ServiceDTOToService;
import appointmentscheduler.dto.employee.EmployeeShiftDTO;
import appointmentscheduler.dto.service.ServiceCreateDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.repository.BusinessRepository;
import appointmentscheduler.repository.EmployeeRepository;
import appointmentscheduler.repository.ServiceRepository;
import appointmentscheduler.serializer.*;
import appointmentscheduler.service.appointment.AppointmentService;
import appointmentscheduler.service.business.BusinessService;
import appointmentscheduler.service.employee.EmployeeShiftService;
import appointmentscheduler.service.service.ServiceService;
import appointmentscheduler.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "${rest.api.path}", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController extends AbstractController {


    // to be redone with serializer
    @Autowired
    private ServiceDTOToService serviceConverter;

    private AppointmentService appointmentService;
    private UserService userService;
    private ServiceService serviceService;
    private ServiceRepository serviceRepository;
    private EmployeeRepository wmployeeRepository;
    private BusinessService businessService;
    private BusinessRepository businessRepository;

    private EmployeeShiftService employeeShiftService;
    private ObjectMapperFactory objectMapperFactory;

    //from ema's branch
    @Autowired
    public AdminController( AppointmentService appointmentService, UserService userService, ServiceService serviceService, ServiceRepository serviceRepository,
                           EmployeeRepository employeeRepository, EmployeeShiftService employeeShiftService,
                            ObjectMapperFactory objectMapperFactory) {
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.serviceService = serviceService;
        this.serviceRepository = serviceRepository;
        this.employeeShiftService = employeeShiftService;
        this.objectMapperFactory = objectMapperFactory;
    }

    @LogREST
    @GetMapping("/business/{businessId}/admin/employees")
    public ResponseEntity<String> getBusinessEmployees(@PathVariable long businessId) {
        List<Employee> employees = employeeShiftService.getEmployeesForBusiness(businessId);
        final ObjectMapper mapper = objectMapperFactory.createMapper(Employee.class, new AdminEmployeeSerializer());
        return getJson(mapper, employees);
    }

    //TODO test this method
    @GetMapping("/business/{businessId]/admin/employee/{id}")
    public ResponseEntity<String> getBusinessEmployees(@PathVariable long businessId, @PathVariable long id) {
        Employee employee = employeeShiftService.getEmployeeByBusinessId(id, businessId);
        final ObjectMapper mapper = objectMapperFactory.createMapper(Employee.class, new AdminEmployeeSerializer());
        return getJson(mapper, employee);
    }

    @GetMapping("/business/{businessId}/admin/employee/{employeeId}/shift")
    public ResponseEntity<String> getBusinessEmployeeShifts(@PathVariable long businessId,
                                                          @PathVariable long employeeId) {
        List<Shift> shifts = employeeShiftService.getEmployeeShiftsForBusiness(employeeId, businessId);
        final ObjectMapper mapper = objectMapperFactory.createMapper(Shift.class, new AdminEmployeeShiftSerializer());
        return getJson(mapper, shifts);
    }

    @LogREST
    @PostMapping("/business/{businessId}/admin/employee/{employeeId}/shift")
    public ResponseEntity<String> createBusinessShift(@PathVariable long employeeId, @PathVariable long businessId,
                                                      @RequestBody EmployeeShiftDTO employeeShiftDTO) {
        Shift shift = employeeShiftService.createShiftForBusiness(employeeId, businessId, employeeShiftDTO);
        final ObjectMapper mapper = objectMapperFactory.createMapper(Shift.class, new AdminEmployeeShiftSerializer());
        return getJson(mapper, shift);
    }

    @LogREST
    @PutMapping("/business/{businessId}/admin/employee/{employeeId}/shift/{shiftId}")
    public ResponseEntity<String> modifyShift(@PathVariable long businessId, @PathVariable long employeeId,
                                              @PathVariable long shiftId,
                                              @RequestBody EmployeeShiftDTO employeeShiftDTO) {
        Shift shift = employeeShiftService.modifyShift(businessId, employeeId, employeeShiftDTO, shiftId);
        final ObjectMapper mapper = objectMapperFactory.createMapper(Shift.class, new AdminEmployeeShiftSerializer());
        return getJson(mapper, shift);
    }

    @LogREST
    @DeleteMapping("/business/{businessId}/admin/employee/shift/{shiftId}")
    public void deleteShift(@PathVariable long businessId, @PathVariable long shiftId){
        employeeShiftService.deleteShift(shiftId, businessId);
    }

    //TODO  can you test the routes from here onwards

    @LogREST
    @GetMapping("/business/{businessId}admin/client/{clientId}")
    public List<Appointment> clientAppointmentList(@PathVariable long businessId, @PathVariable long clientId){
        return this.appointmentService.findByClientIdAndBusinessId(clientId, businessId);
    }

    @GetMapping("/business/{businessId}/admin/employee/appointments/{employeeId}")
    public List<Appointment> employeeAppointmentList( @PathVariable long businessId, @PathVariable long employeeId) {
        return this.appointmentService.findByEmployeeIdAndBusinessId(employeeId, businessId);
    }

    @LogREST
    @GetMapping("/business/{businessId/admin/users")
    public ResponseEntity<String> getAllUsers(@PathVariable long businessId){
        ObjectMapper mapper = objectMapperFactory.createMapper(User.class, new UserSerializer());
        return getJson(mapper, userService.findAllByBusinessId(businessId));
    }

    @LogREST
    @GetMapping("/admin/appointments")
    public ResponseEntity<String> getAllAppointments(@PathVariable long businessId){
        ObjectMapper mapper = objectMapperFactory.createMapper(Appointment.class, new UserAppointmentSerializer());
        return getJson(mapper, appointmentService.findByBusinessId(businessId));
    }

    //todo change to new backend
//todo refactor to use existing code , user table doesnt have businessId
   /* @PostMapping("/admin/user/employee/{id}")
    public ResponseEntity<Map<String, String>> changeRoleToEmployee(@PathVariable long businessId,
                                                                    @PathVariable long id){
        User user = this.userService.findUserByIdAndBusinessId(id, businessId);
        Set<Role> roles = user.getRoles();
        for (Role role: roles) {
            if (role.getRole() == RoleEnum.EMPLOYEE) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }
        user.addRoles(this.roleRepository.findByRole(RoleEnum.EMPLOYEE));
        return ResponseEntity.ok(userService.updateUser(user));
    }*/
    //todo change to new backend
   /* private boolean containAnyRole(Set<Role> roles, RoleEnum roleType) {
        return roles.stream().anyMatch(role -> role.getRole() == roleType);
    }
*/
    //todo change to new backend , user table doesnt have businessId
    // for assigning services to employees (employees can perform certain services)
    /*@PostMapping("business/{businessId}/admin/service/{employeeId}/{serviceId}")
    public ResponseEntity<Map<String, String>> assignService(@PathVariable long businessId,
                                                             @PathVariable long employeeId, @PathVariable long serviceId){
        User user = this.userService.findUserByIdAndBusinessId(employeeId, businessId);
        Set<Role> roles = user.getRoles();
        //check if user is an employee
        return serviceRepository.findById(serviceId).map(service -> {
            user.addEmployeeService(service);
            return ResponseEntity.ok(message("service assigned"));
        }).orElseThrow(() -> new ResourceNotFoundException(String.format("Service id %d not found.", serviceId)));
    }*/


    // for adding a new service
    @LogREST
    @PostMapping("/business/{businessId}/admin/service")
    public ResponseEntity<Map<String, String>> add(@PathVariable long businessId,
                                                   @RequestBody ServiceCreateDTO serviceCreateDTO){
        Service service = serviceConverter.convert(serviceCreateDTO);
        Business business = businessService.findById(businessId);
        if (business!=null){
            service.setBusiness(business);
        }
        return ResponseEntity.ok(serviceService.add(service));
    }
    private Map<String, String> message(String message) {
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        return map;
    }

}
