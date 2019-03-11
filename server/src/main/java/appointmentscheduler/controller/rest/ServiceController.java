package appointmentscheduler.controller.rest;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.repository.ServiceRepository;
import appointmentscheduler.serializer.ObjectMapperFactory;
import appointmentscheduler.serializer.ServiceSerializer;
import appointmentscheduler.serializer.UserAppointmentSerializer;
import appointmentscheduler.service.service.ServiceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("${rest.api.path}")
@PreAuthorize("hasAuthority('CLIENT')")
public class ServiceController {

    private final ServiceRepository serviceRepository;
    private final ServiceService serviceService;
    private final ObjectMapperFactory objectMapperFactory;

    @Autowired
    public ServiceController(ServiceRepository serviceRepository, ObjectMapperFactory objectMapperFactory) {
        this.serviceRepository = serviceRepository;
        this.serviceService = new ServiceService(serviceRepository);
        this.objectMapperFactory = objectMapperFactory;
    }

    @GetMapping(value = "/services", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findAll() {
        final ObjectMapper mapper = objectMapperFactory.createMapper(Service.class, new ServiceSerializer());

        try {
            return ResponseEntity.ok(mapper.writeValueAsString(serviceRepository.findAll()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/business/{businessId}/services", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findAll(@PathVariable long businessId) {
        final ObjectMapper mapper = objectMapperFactory.createMapper(Service.class, new ServiceSerializer());

        try {
            return ResponseEntity.ok(mapper.writeValueAsString(serviceService.findByBusinessId(businessId)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/business/{businessId}/services/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findById(@PathVariable long businessId, @PathVariable long id) {
        final ObjectMapper mapper = objectMapperFactory.createMapper(Service.class, new ServiceSerializer());

        try {
            final Service service = serviceService.findByIdAndBusinessId(id, businessId);
            return ResponseEntity.ok(mapper.writeValueAsString(service));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/services/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findById(@PathVariable long id) {
        final ObjectMapper mapper = objectMapperFactory.createMapper(Appointment.class, new UserAppointmentSerializer());

        try {
            final Service service = serviceRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
            return ResponseEntity.ok(mapper.writeValueAsString(service));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public Object add(Object o) {
        // todo
        return null;
    }

    public Object update(long id, Object o) {
        // todo
        return null;
    }

    public ResponseEntity delete(long id) {
        // todo
        return null;
    }
}
