package appointmentscheduler.controller.rest;

import appointmentscheduler.annotation.LogREST;
import appointmentscheduler.dto.appointment.AppointmentDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.appointment.CancelledAppointment;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.repository.EmployeeRepository;
import appointmentscheduler.repository.ServiceRepository;
import appointmentscheduler.repository.UserRepository;
import appointmentscheduler.serializer.*;
import appointmentscheduler.service.appointment.AppointmentService;
import appointmentscheduler.service.business.BusinessService;
import appointmentscheduler.service.email.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(value = "/${rest.api.path}/business", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppointmentController extends AbstractController {

   
    private final AppointmentService appointmentService;
    private final ObjectMapperFactory objectMapperFactory;


    @Autowired
    public AppointmentController(
            AppointmentService appointmentService,  ObjectMapperFactory objectMapperFactory
    ) {
        this.appointmentService = appointmentService;
        this.objectMapperFactory = objectMapperFactory;
    }


    @PostMapping("/{businessId}/appointments")
    public ResponseEntity<String> addAppointmentToBusiness(@RequestBody AppointmentDTO appointmentDTO, @PathVariable long businessId){
        final ObjectMapper mapper = objectMapperFactory.createMapper(Appointment.class, new UserAppointmentSerializer());
        Appointment savedAppointment = appointmentService.add(appointmentDTO, getUserId(), businessId);

        return getJson(mapper, savedAppointment);
    }

    @PostMapping("/{businessId}/appointments-list")
    public ResponseEntity<String> addAppointmentToBusiness(@RequestBody List<AppointmentDTO> appointmentDTOS, @PathVariable long businessId){
        final ObjectMapper mapper = objectMapperFactory.createMapper(Appointment.class, new UserAppointmentSerializer());
        List<Appointment> savedAppointment = appointmentService.addList(appointmentDTOS, getUserId(), businessId);

        return getJson(mapper, savedAppointment);
    }

    @PutMapping("/{businessId}/appointments/{id}")
    public ResponseEntity<String> update(@PathVariable long businessId, @PathVariable long appointmentId, @RequestBody AppointmentDTO appointmentDTO) {
        final ObjectMapper mapper = objectMapperFactory.createMapper(Appointment.class, new UserAppointmentSerializer());
        Appointment modifiedAppointment = appointmentService.update(appointmentDTO, getUserId(), businessId, appointmentId);

        return getJson(mapper, modifiedAppointment);
    }

    //Not delete mapping because method changes appointment status to cancelled, therefore doesn't delete the appointment
    @PutMapping("{businessId}/appointments/cancel/{id}")
    public ResponseEntity cancelAppointment(@PathVariable long id, @PathVariable long businessId){
        final ObjectMapper mapper = objectMapperFactory.createMapper(Appointment.class, new UserAppointmentSerializer());
        Appointment cancelledAppointment = appointmentService.cancel(id,businessId, getUserId());

        return getJson(mapper, cancelledAppointment);
    }

    @LogREST
    @GetMapping("/{businessId}/employee/{serviceId}")
    public ResponseEntity<String> getAvailableEmployeesByServiceAndByDate(@PathVariable long serviceId, @RequestParam String date) {
        ObjectMapper mapper = objectMapperFactory.createMapper(Employee.class, new EmployeeSerializer());
        return getJson(mapper, appointmentService.getAvailableEmployeesByServiceAndByDate(serviceId, date));
    }

    @LogREST
    @GetMapping("{businessId}/employee/shifts")
    public ResponseEntity<String> getEmployeesShift(@RequestParam String date, @PathVariable long businessId) {
        ObjectMapper mapper = objectMapperFactory.createMapper(Shift.class, new ShiftSerializer());
        return getJson(mapper, appointmentService.getEmployeeShiftsByDateAndBusinessId(date, businessId));
    }

    @LogREST
    @GetMapping("{businessId}/employee/appointments")
    public ResponseEntity<String> getEmployeesConfirmedAppointments(@RequestParam String date, @PathVariable long businessId) {
        ObjectMapper objectMapper = objectMapperFactory.createMapper(Appointment.class, new EmployeeAppointmentSerializer());
        return getJson(objectMapper, appointmentService.getConfirmedAppointmentsByDateAndBusinessId(date,businessId));
    }


    @GetMapping("{businessId}/cancel/{id}")
    public ResponseEntity<String> findId(@PathVariable long id, @PathVariable long businessId){
        ObjectMapper mapper = objectMapperFactory.createMapper(CancelledAppointment.class, new CancelledAppointmentSerializer());
        return getJson(mapper, appointmentService.findByCancelledIdAndBusinessId(id,  businessId));
    }


}
