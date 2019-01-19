package appointmentscheduler.controller.rest;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.service.appointment.AppointmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${rest.api.path}/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private AppointmentService appointmentService;

    public AdminController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }


    // TODO remove this
    @GetMapping
    public String areYouAnAdmin(@RequestAttribute long userId) {
        return String.format("You are an admin, your id is %d.", userId);
    }

    @GetMapping("/employee/{id}")
    public List<Appointment> employeeAppointmentList(@RequestAttribute long employeeId) {
        return this.appointmentService.findByEmployeeId(employeeId);
    }
}
