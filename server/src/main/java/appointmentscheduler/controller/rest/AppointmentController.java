package appointmentscheduler.controller.rest;

import appointmentscheduler.converters.appointment.AppointmentDTOToAppointment;
import appointmentscheduler.dto.appointment.AppointmentDTO;
import appointmentscheduler.entity.appointment.Appointment;
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

    //TODO  change it sections
    @GetMapping("/current_user")
    public List<Appointment> findByCurrentUser() {return appointmentService.findByClientId(getUserId());
    }

    @PostMapping
    @Override
    public Appointment add(@RequestBody AppointmentDTO appointmentDTO) {
        appointmentDTO.setClientId(getUserId());
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
