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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/${rest.api.path}/appointments")
public class AppointmentController extends AbstractController {

    private final AppointmentService appointmentService;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final ServiceRepository serviceRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapperFactory objectMapperFactory;

    @Autowired
    public AppointmentController(
            AppointmentService appointmentService, UserRepository userRepository, ServiceRepository serviceRepository,
            ModelMapper modelMapper, EmployeeRepository employeeRepository, ObjectMapperFactory objectMapperFactory
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
    }

    @GetMapping
    public List<Appointment> findAll() {
        return appointmentService.findAll();
    }

    @GetMapping("/{id}")
    public Appointment findById(@PathVariable long id) {
        return appointmentService.findById(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> add(@RequestBody AppointmentDTO appointmentDTO) {
        Appointment appointment = modelMapper.map(appointmentDTO, Appointment.class);

        User client = userRepository.findById(getUserId()).orElseThrow(ResourceNotFoundException::new);
        appointment.setClient(client);

        Employee employee = employeeRepository.findById(appointmentDTO.getEmployeeId()).orElseThrow(ResourceNotFoundException::new);
        appointment.setEmployee(employee);

        Service service = serviceRepository.findById(appointmentDTO.getServiceId()).orElseThrow(ResourceNotFoundException::new);
        appointment.setService(service);

        final ObjectMapper mapper = objectMapperFactory.createMapper(Appointment.class, new UserAppointmentSerializer());
        return getJson(mapper, appointmentService.add(appointment));
    }

    @PutMapping("/{id}")
    public Appointment update(@PathVariable long id, @RequestBody AppointmentDTO appointmentDTO) {
        // todo not implemented
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable long id) {
        return appointmentService.cancel(id);
    }

    @GetMapping(value = "/employees", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAvailableEmployees(@RequestParam String date) {
        LocalDate pickedDate = parseDate(date);
        ObjectMapper mapper = objectMapperFactory.createMapper(Employee.class, new EmployeeSerializer());
        return getJson(mapper, appointmentService.getAvailableEmployees(pickedDate));
    }

    @GetMapping(value = "employee/{employeeId}/shift", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getEmployeesShift(@PathVariable long employeeId, @RequestParam String date) {
        LocalDate pickedDate = parseDate(date);
        ObjectMapper mapper = objectMapperFactory.createMapper(Shift.class, new ShiftSerializer());
        return getJson(mapper, appointmentService.getEmployeesShiftByDate(employeeId, pickedDate));
    }

    @GetMapping(value = "employee/{employeeId}/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getEmployeesConfirmedAppointments(@PathVariable long employeeId, @RequestParam String date) {
        LocalDate pickedDate = parseDate(date);
        ObjectMapper objectMapper = objectMapperFactory.createMapper(Appointment.class, new EmployeeAppointmentSerializer());
        return getJson(objectMapper, appointmentService.getEmployeesConfirmedAppointmentsByDate(employeeId, pickedDate));
    }

    private LocalDate parseDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("M/d/yyyy"));
    }

}
