package appointmentscheduler.controller.rest;


import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.repository.BusinessRepository;
import appointmentscheduler.repository.ServiceRepository;
import appointmentscheduler.serializer.BusinessSerializer;
import appointmentscheduler.serializer.ObjectMapperFactory;
import appointmentscheduler.serializer.ServiceSerializer;
import appointmentscheduler.serializer.UserAppointmentSerializer;
import appointmentscheduler.service.business.BusinessService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("${rest.api.path}/businesses")
public class BusinessController extends AbstractController {

    private final BusinessRepository businessRepository;
    private final ObjectMapperFactory objectMapperFactory;
    private final BusinessService businessService;

    @Autowired
    public BusinessController(BusinessService businessService, BusinessRepository businessRepository,
                              ObjectMapperFactory objectMapperFactory) {

        this.businessService = businessService;
        this.businessRepository = businessRepository;
        this.objectMapperFactory = objectMapperFactory;
    }

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
            return ResponseEntity.ok(mapper.writeValueAsString(businessRepository.findById(id)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    //    return getJson(mapper, businessService.findById(id));
    }
    //TODO Create GET


    //TODO Create POST
}
