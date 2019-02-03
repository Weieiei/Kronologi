package appointmentscheduler.controller.rest;

import appointmentscheduler.dto.service.ServiceCreateDTO;
import appointmentscheduler.converters.service.ServiceDTOToService;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.role.Role;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.service.ServiceEntity;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.repository.RoleRepository;
import appointmentscheduler.repository.ServiceRepository;
import appointmentscheduler.service.appointment.AppointmentService;
import appointmentscheduler.service.service.ServiceService;
import appointmentscheduler.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("${rest.api.path}/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {


    @Autowired
    private ServiceDTOToService serviceConverter;

    private AppointmentService appointmentService;
    private UserService userService;
    private RoleRepository roleRepository;
    private ServiceService serviceService;

    public AdminController(AppointmentService appointmentService, UserService userService, ServiceService serviceService, RoleRepository roleRepository, ServiceRepository serviceRepository) {
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.serviceService = serviceService;
        this.roleRepository = roleRepository;

    }

    // TODO remove this
    @GetMapping
    public String areYouAnAdmin(@RequestAttribute long userId) {
        return String.format("You are an admin, your id is %d.", userId);
    }

    @GetMapping("/client/{id}")
    public List<Appointment> clientAppointmentList(@PathVariable long clientId){
        return this.appointmentService.findByClientId(clientId);
    }

    @GetMapping("/employee/{id}")
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


    @PostMapping("/user/employee")
    public ResponseEntity<Map<String, Object>> changeRoleToEmployee(@RequestAttribute long id){
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

    // for assigning services to employees (employees can perform certain services)
    @PostMapping("service/{id}")
    public void assignService(@PathVariable long id, ServiceEntity employeeService){
        User user = this.userService.findUserByid(id);
        Set<Role> roles = user.getRoles();
        //check if user is an employee
        if (roles.contains(this.roleRepository.findByRole(RoleEnum.EMPLOYEE))) {
            //check if the employee can already perform the service
            if (user.getEmployeeServices().contains(employeeService)){
                System.out.println("The employee has already been assigned that service");
            }
            else {
                // assign the service
                user.addEmployeeService(employeeService);
            }
        }
        else {
            System.out.println("The user is not an employee, and therefore cannot be assigned a service");
        }
    }

    // for adding a new service
    @PostMapping("service")
    public ServiceEntity addService(@RequestBody ServiceCreateDTO serviceDTO){
        ServiceEntity service = serviceConverter.convert(serviceDTO);
        return serviceService.add(service);
    }


}
