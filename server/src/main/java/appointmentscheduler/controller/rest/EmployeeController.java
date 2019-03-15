package appointmentscheduler.controller.rest;

import appointmentscheduler.annotation.LogREST;
import appointmentscheduler.converters.appointment.CancelledDTOToCancelled;
import appointmentscheduler.dto.appointment.CancelAppointmentDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.appointment.CancelledAppointment;
import appointmentscheduler.serializer.ObjectMapperFactory;
import appointmentscheduler.serializer.UserAppointmentSerializer;
import appointmentscheduler.service.appointment.AppointmentService;
import appointmentscheduler.service.business.BusinessService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/${rest.api.path}/business/employee")
@PreAuthorize("hasAuthority('EMPLOYEE')")
public class EmployeeController  extends AbstractController {

    private final AppointmentService appointmentService;
    private final ObjectMapperFactory objectMapperFactory;
    private final BusinessService businessService;
    private CancelledDTOToCancelled cancelledAppointmentConverted;

    @Autowired
    public EmployeeController(AppointmentService appointmentService, ObjectMapperFactory objectMapperFactory, CancelledDTOToCancelled cancelledAppointmentConverted, BusinessService businessService) {
        this.cancelledAppointmentConverted = cancelledAppointmentConverted;
        this.appointmentService = appointmentService;
        this.objectMapperFactory = objectMapperFactory;
        this.businessService = businessService;
    }


    @LogREST
    @GetMapping(value = "/{businessId}/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getEmployeeAppointments(@PathVariable long businessId) {
        List<Appointment> listOfAppointment = appointmentService.findByBusinessIdAndEmployeeId(businessId, getUserId());
        listOfAppointment.sort(Comparator.comparing(Appointment::getStartTime)
                .thenComparing(Appointment::getDate));
        final ObjectMapper mapper = objectMapperFactory.createMapper(Appointment.class, new UserAppointmentSerializer());
        return getJson(mapper, listOfAppointment);
    }

    @LogREST
    @PostMapping("{businessId}/appointments/cancel")
    public ResponseEntity delete(@RequestBody CancelAppointmentDTO cancel) {
        cancel.setIdPersonWhoCancelled(getUserId());
        CancelledAppointment cancelled = cancelledAppointmentConverted.convert(cancel);
        return ResponseEntity   .ok(appointmentService.cancel(cancelled));
    }
}
