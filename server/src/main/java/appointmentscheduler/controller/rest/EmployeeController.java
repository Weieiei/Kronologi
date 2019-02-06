package appointmentscheduler.controller.rest;

import appointmentscheduler.annotation.LogREST;
import appointmentscheduler.converters.appointment.CancelledDTOToCancelled;
import appointmentscheduler.dto.appointment.CancelAppointmentDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.appointment.CancelledAppointment;
import appointmentscheduler.service.appointment.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/${rest.api.path}/employee")
@PreAuthorize("hasAuthority('EMPLOYEE')")
public class EmployeeController  extends AbstractController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private CancelledDTOToCancelled cancelledAppointmentConverted;

    @LogREST
    @GetMapping("/appointments")
    public List<Appointment> findByCurrentEmployee() {
        List<Appointment> listOfAppointment = appointmentService.findByEmployeeId(getUserId());
        listOfAppointment.sort(Comparator.comparing(Appointment::getDate));
        return listOfAppointment;
    }

    @LogREST
    @PostMapping("/appointments/cancel")
    public ResponseEntity delete(@RequestBody CancelAppointmentDTO cancel) {
        cancel.setIdPersonWhoCancelled(getUserId());
        CancelledAppointment cancelled = cancelledAppointmentConverted.convert(cancel);
        return appointmentService.cancel(cancelled);
    }
}
