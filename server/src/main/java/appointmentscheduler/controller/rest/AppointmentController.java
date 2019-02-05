package appointmentscheduler.controller.rest;

import appointmentscheduler.dto.appointment.AppointmentDTO;
import appointmentscheduler.entity.appointment.Appointment;
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
import appointmentscheduler.service.email.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping(value = "/${rest.api.path}/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppointmentController extends AbstractController {

    private final AppointmentService appointmentService;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final ServiceRepository serviceRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapperFactory objectMapperFactory;
    private final EmailService emailService;

    @Autowired
    public AppointmentController(
            AppointmentService appointmentService, UserRepository userRepository, ServiceRepository serviceRepository,
            ModelMapper modelMapper, EmployeeRepository employeeRepository, ObjectMapperFactory objectMapperFactory,
            EmailService emailService
    ) {
        this.appointmentService = appointmentService;
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
        this.modelMapper = modelMapper;
        this.employeeRepository = employeeRepository;
        this.modelMapper.addMappings(new PropertyMap<AppointmentDTO, Appointment>() {
            protected void configure() {
                skip().setId(0);
            }
        });
        this.objectMapperFactory = objectMapperFactory;
        this.emailService = emailService;
    }

    private Appointment mapAppointmentDTOToAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = modelMapper.map(appointmentDTO, Appointment.class);

        User client = userRepository.findById(getUserId()).orElseThrow(ResourceNotFoundException::new);
        appointment.setClient(client);

        Employee employee = employeeRepository.findById(appointmentDTO.getEmployeeId()).orElseThrow(ResourceNotFoundException::new);
        appointment.setEmployee(employee);

        Service service = serviceRepository.findById(appointmentDTO.getServiceId()).orElseThrow(ResourceNotFoundException::new);
        appointment.setService(service);

        return appointment;
    }

    @PostMapping
    public ResponseEntity<String> add(@RequestBody AppointmentDTO appointmentDTO) throws MessagingException {
        Appointment appointment = mapAppointmentDTOToAppointment(appointmentDTO);
        final ObjectMapper mapper = objectMapperFactory.createMapper(Appointment.class, new UserAppointmentSerializer());
        Appointment savedAppointment = appointmentService.add(appointment);
        sendConfirmationMessage(savedAppointment, false);
        return getJson(mapper, savedAppointment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable long id, @RequestBody AppointmentDTO appointmentDTO) throws MessagingException {
        Appointment appointment = mapAppointmentDTOToAppointment(appointmentDTO);
        final ObjectMapper mapper = objectMapperFactory.createMapper(Appointment.class, new UserAppointmentSerializer());
        Appointment modifiedAppointment = appointmentService.update(id, appointment);
        sendConfirmationMessage(modifiedAppointment, true);
        return getJson(mapper, modifiedAppointment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable long id) throws MessagingException {
        final ObjectMapper mapper = objectMapperFactory.createMapper(Appointment.class, new UserAppointmentSerializer());
        Appointment cancelledAppointment = appointmentService.cancel(id, getUserId());
        sendCancellationMessage(cancelledAppointment);
        return getJson(mapper, cancelledAppointment);
    }

    @GetMapping("/employees/{serviceId}")
    public ResponseEntity<String> getAvailableEmployeesByServiceAndByDate(@PathVariable long serviceId, @RequestParam String date) {
        LocalDate pickedDate = parseDate(date);
        ObjectMapper mapper = objectMapperFactory.createMapper(Employee.class, new EmployeeSerializer());
        return getJson(mapper, appointmentService.getAvailableEmployeesByServiceAndByDate(serviceId, pickedDate));
    }

    @GetMapping("employee/{employeeId}/shift")
    public ResponseEntity<String> getEmployeesShift(@PathVariable long employeeId, @RequestParam String date) {
        LocalDate pickedDate = parseDate(date);
        ObjectMapper mapper = objectMapperFactory.createMapper(Shift.class, new ShiftSerializer());
        return getJson(mapper, appointmentService.getEmployeesShiftByDate(employeeId, pickedDate));
    }

    @GetMapping("employee/{employeeId}/appointments")
    public ResponseEntity<String> getEmployeesConfirmedAppointments(@PathVariable long employeeId, @RequestParam String date) {
        LocalDate pickedDate = parseDate(date);
        ObjectMapper objectMapper = objectMapperFactory.createMapper(Appointment.class, new EmployeeAppointmentSerializer());
        return getJson(objectMapper, appointmentService.getEmployeesConfirmedAppointmentsByDate(employeeId, pickedDate));
    }

    private LocalDate parseDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("M/d/yyyy"));
    }

    private void sendConfirmationMessage(Appointment appointment, boolean modifying) throws MessagingException {

        String message = String.format(
                "Hello %1$s,<br><br>" +
                        "Your reservation at Sylvia Pizzi Spa has been " + (modifying ? "modified" : "confirmed") + ".<br><br>" +
                        "%2$s with %3$s<br>" +
                        "%4$s at %5$s<br><br>" +
                        "We look forward to seeing you!",
                appointment.getClient().getFirstName(),
                appointment.getService().getName(), appointment.getEmployee().getFullName(),
                appointment.getDate().format(DateTimeFormatter.ofPattern("MMMM dd yyyy")), appointment.getStartTime().toString()
        );

        emailService.sendEmail(appointment.getClient().getEmail(), "ASApp Appointment Confirmation", message, true);

    }

    private void sendCancellationMessage(Appointment appointment) throws MessagingException {

        String message = String.format(
                "Hello %1$s,<br><br>" +
                        "Your reservation at Sylvia Pizzi Spa has been cancelled.<br>",
                appointment.getClient().getFirstName()
        );

        emailService.sendEmail(appointment.getClient().getEmail(), "ASApp Appointment Confirmation", message, true);

    }

}
