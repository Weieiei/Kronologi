package appointmentscheduler.controller.rest;


import appointmentscheduler.dto.business.BusinessDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.repository.BusinessRepository;
import appointmentscheduler.serializer.BusinessSerializer;
import appointmentscheduler.serializer.ObjectMapperFactory;
import appointmentscheduler.serializer.UserAppointmentSerializer;
import appointmentscheduler.service.business.BusinessService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.mail.MessagingException;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("${rest.api.path}/businesses")
public class BusinessController extends AbstractController {

    private final BusinessRepository businessRepository;
    private final ObjectMapperFactory objectMapperFactory;
    private final BusinessService businessService;
    private final ModelMapper modelMapper;

    @Autowired
    public BusinessController(BusinessService businessService, BusinessRepository businessRepository,
                              ObjectMapperFactory objectMapperFactory, ModelMapper modelMapper) {

        this.businessService = businessService;
        this.businessRepository = businessRepository;
        this.objectMapperFactory = objectMapperFactory;
        this.modelMapper = modelMapper;
    }
//TODO figure out how the buisness id will be handled
//    private Business mapBusinessDTOToBusiness(BusinessDTO businessDTO){
 //       Business business = modelMapper.map(businessDTO, Business.class);

//        Business business = businessRepository.findById(getUserId()).orElseThrow(ResourceNotFoundException::new);
//        appointment.setClient(client);
//
//        Employee employee = employeeRepository.findById(appointmentDTO.getEmployeeId()).orElseThrow(ResourceNotFoundException::new);
//        appointment.setEmployee(employee);
//
//        Service service = serviceRepository.findById(appointmentDTO.getServiceId()).orElseThrow(ResourceNotFoundException::new);
//        appointment.setService(service);
//
//        return appointment;
//    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findAll() {
        final ObjectMapper mapper = objectMapperFactory.createMapper(Business.class, new BusinessSerializer());

        try {
            return ResponseEntity.ok(mapper.writeValueAsString(businessRepository.findAll()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> findById(@PathVariable long id) {
        final ObjectMapper mapper = objectMapperFactory.createMapper(Appointment.class, new UserAppointmentSerializer());
        try {
            return ResponseEntity.ok(mapper.writeValueAsString(businessService.findById(id)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    //TODO Create POST

//    @PostMapping
//    public ResponseEntity<String> add(@RequestBody AppointmentDTO appointmentDTO) throws MessagingException {
//        Appointment appointment = mapAppointmentDTOToAppointment(appointmentDTO);
//        final ObjectMapper mapper = objectMapperFactory.createMapper(Appointment.class, new UserAppointmentSerializer());
//        Appointment savedAppointment = appointmentService.add(appointment);
//        sendConfirmationMessage(savedAppointment, false);
//        return getJson(mapper, savedAppointment);
//    }

    //TODO Create POST

}
