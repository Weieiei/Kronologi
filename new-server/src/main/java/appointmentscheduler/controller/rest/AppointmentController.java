package appointmentscheduler.controller.rest;

import appointmentscheduler.converters.appointment.AppointmentDTOToAppointment;
import appointmentscheduler.dto.AppointmentDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.service.appointment.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${rest.api.path}/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentDTOToAppointment appointmentConverter;

    @GetMapping
    public Page<Appointment> findAll(Pageable pageable) {
        return appointmentService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Appointment findById(@PathVariable long id) {
        return appointmentService.findById(id);
    }

    @PostMapping
    public Appointment add(@RequestBody AppointmentDTO appointmentDTO) {
        Appointment appointment = appointmentConverter.convert(appointmentDTO);
        return appointmentService.add(appointment);
    }

    @PutMapping("/{id}")
    public Appointment update(@PathVariable long id, @RequestBody AppointmentDTO appointmentDTO) {
        Appointment appointment = appointmentConverter.convert(appointmentDTO);
        return appointmentService.update(id, appointment);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable long id) {
        return appointmentService.cancel(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        return appointmentService.delete(id);
    }

}
