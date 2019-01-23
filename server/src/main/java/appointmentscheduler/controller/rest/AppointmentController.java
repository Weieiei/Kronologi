package appointmentscheduler.controller.rest;

import appointmentscheduler.dto.appointment.AppointmentDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.repository.EmployeeRepository;
import appointmentscheduler.repository.ServiceRepository;
import appointmentscheduler.repository.UserRepository;
import appointmentscheduler.service.AuthenticationService;
import appointmentscheduler.service.appointment.AppointmentService;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${rest.api.path}/appointments")
public class AppointmentController extends IRestController<Appointment, AppointmentDTO> {

    private final AppointmentService appointmentService;

    private final AuthenticationService authenticationService;

    private final UserRepository userRepository;

    private final EmployeeRepository employeeRepository;

    private final ServiceRepository serviceRepository;

    private ModelMapper modelMapper;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, AuthenticationService authenticationService, UserRepository userRepository, ServiceRepository serviceRepository, ModelMapper modelMapper, EmployeeRepository employeeRepository) {
        this.appointmentService = appointmentService;
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
        this.modelMapper = modelMapper;
        this.employeeRepository = employeeRepository;

        this.modelMapper.addMappings(new PropertyMap<AppointmentDTO, Appointment>() {
            protected void configure() {
                skip().setId(0);
            }
        });
    }

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
        long clientId = authenticationService.getCurrentUserId();

        Appointment appointment = modelMapper.map(appointmentDTO, Appointment.class);

        User client = userRepository.findById(clientId).orElseThrow(ResourceNotFoundException::new);
        appointment.setClient(client);

        Employee employee = employeeRepository.findById(appointmentDTO.getEmployeeId()).orElseThrow(ResourceNotFoundException::new);
        appointment.setEmployee(employee);

        Service service = serviceRepository.findById(appointmentDTO.getServiceId()).orElseThrow(ResourceNotFoundException::new);
        appointment.setService(service);

        return appointmentService.add(appointment);
    }

    @PutMapping("/{id}")
    @Override
    public Appointment update(@PathVariable long id, @RequestBody AppointmentDTO appointmentDTO) {
        // todo not implemented
        return null;
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity delete(@PathVariable long id) {
        return appointmentService.cancel(id);
    }

}
