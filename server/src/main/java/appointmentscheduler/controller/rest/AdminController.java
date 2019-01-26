package appointmentscheduler.controller.rest;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.role.Role;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.repository.RoleRepository;
import appointmentscheduler.service.appointment.AppointmentService;
import appointmentscheduler.service.user.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("${rest.api.path}/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private AppointmentService appointmentService;
    private UserService userService;
    private RoleRepository roleRepository;

    public AdminController(AppointmentService appointmentService, UserService userService, RoleRepository roleRepository) {
        this.appointmentService = appointmentService;
        this.userService = userService;
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
}
