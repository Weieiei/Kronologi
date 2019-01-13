package appointmentscheduler.controller.rest;

import appointmentscheduler.converters.appointment.AppointmentDTOToAppointment;
import appointmentscheduler.dto.appointment.AppointmentDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.service.AuthenticationService;
import appointmentscheduler.service.appointment.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/${rest.api.path}/appointments")
public class AppointmentController extends IRestController<Appointment, AppointmentDTO> {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentDTOToAppointment appointmentConverter;

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping
    @Override
    public List<Appointment> findAll() {
        return appointmentService.findAll();
    }

    @GetMapping("/{id}")
    @Override
    public Appointment findById(@PathVariable long id) {
        return appointmentService.findById(id);
    }

    @PostMapping
    @Override
    public Appointment add(@RequestBody AppointmentDTO appointmentDTO) {
        long userId = authenticationService.getCurrentUserId();

        Appointment appointment = appointmentConverter.convert(appointmentDTO);
        return appointmentService.add(appointment);
    }

    @PutMapping("/{id}")
    @Override
    public Appointment update(@PathVariable long id, @RequestBody AppointmentDTO appointmentDTO) {
        Appointment appointment = appointmentConverter.convert(appointmentDTO);
        return appointmentService.update(id, appointment);
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity delete(@PathVariable long id) {
        return appointmentService.cancel(id);
    }

}
