package appointmentscheduler.controller.rest;

import appointmentscheduler.annotation.LogREST;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.service.appointment.AppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/${rest.api.path}/employee")
@PreAuthorize("hasAuthority('EMPLOYEE')")
public class EmployeeController extends AbstractController {

    @Autowired
    private AppointmentService appointmentService;

    @LogREST
    @GetMapping("/appointments")
    public List<Appointment> findByCurrentEmployee() {
        List<Appointment> listOfAppointment = appointmentService.findByEmployeeId(getUserId());
        listOfAppointment.sort(Comparator.comparing(Appointment::getDate));
        return listOfAppointment;
    }

    @LogREST
    @DeleteMapping("/appointments/{id}")
    public ResponseEntity delete(@PathVariable long id) {
        return appointmentService.cancel(id);
    }
}
