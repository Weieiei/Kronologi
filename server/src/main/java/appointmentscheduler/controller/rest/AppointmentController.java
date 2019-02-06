package appointmentscheduler.controller.rest;

import appointmentscheduler.annotation.LogREST;
import appointmentscheduler.converters.appointment.AppointmentDTOToAppointment;
import appointmentscheduler.dto.appointment.AppointmentDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.appointment.CancelledAppointment;
import appointmentscheduler.service.appointment.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
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
        List<Appointment> listOfAppointment = appointmentService.findAll();
        listOfAppointment.sort(Comparator.comparing(Appointment::getDate));

        return listOfAppointment;
    }

    @GetMapping("/{id}")
    @Override
    public Appointment findById(@PathVariable long id) {
        return appointmentService.findById(id);
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

    @LogREST
    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity delete(@PathVariable long id) {
        return appointmentService.delete(id);
    }

    @GetMapping("cancel/{id}")
    public CancelledAppointment findId(@PathVariable long id){ return appointmentService.findByCancelledId(id); }


}
