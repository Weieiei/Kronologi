package appointmentscheduler.controller.rest;

import appointmentscheduler.dto.service.ServiceDTO;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("${rest.api.path}/admin")
@PreAuthorize("hasRole('ADMIN')")
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
    public List<Appointment> clientAppointmentList(@RequestAttribute long clientId){
        return this.appointmentService.findByClientId(clientId);
    }

    @GetMapping("/employee/{id}")
    public List<Appointment> employeeAppointmentList(@RequestAttribute long employeeId) {
        return this.appointmentService.findByEmployeeId(employeeId);
    }

    @GetMapping("/appointments")
    public List<Appointment> getAllAppointments(){
        return appointmentService.findAll();
    }

    @PostMapping("/change_to_employee/{id}")
    public String changeRoleToEmployee(@RequestAttribute long id){
        User user = this.userService.findUserByid(id);
        Set<Role> roles = user.getRoles();
        for (Role role: roles) {
            if (role.getRole() == RoleEnum.EMPLOYEE) {
                return "User " + id + " is already an employee";
            }
        }
        user.addRoles(this.roleRepository.findByRole(RoleEnum.EMPLOYEE));
        if (userService.updateUser(user))
            return "Successfully changed user to employee.";
        return "Something went wrong while updating user, please check the server log.";
    }

    // for assigning services to employees (employees can perform certain services)
    @PostMapping("assign_service/{id}")
    public void assignService(@RequestAttribute long id, ServiceEntity employeeService){
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
    @PostMapping("add_service/{id}")
    public ServiceEntity addService(@RequestBody ServiceDTO serviceDTO){
        ServiceEntity service = serviceConverter.convert(serviceDTO);
        return serviceService.add(service);
    }


}
