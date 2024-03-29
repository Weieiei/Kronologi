package appointmentscheduler.controller.rest;

import appointmentscheduler.annotation.LogREST;
import appointmentscheduler.converters.service.ServiceDTOToService;
import appointmentscheduler.dto.service.ServiceCreateDTO;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.file.ServiceFile;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.exception.FileStorageException;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.repository.BusinessRepository;
import appointmentscheduler.repository.ServiceRepository;
import appointmentscheduler.serializer.ObjectMapperFactory;
import appointmentscheduler.serializer.ServiceSerializer;
import appointmentscheduler.service.business.BusinessService;
import appointmentscheduler.service.file.ServiceFileStorageService;
import appointmentscheduler.service.service.ServiceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${rest.api.path}/business")

public class ServiceController {

    private final ServiceRepository serviceRepository;
    private final BusinessRepository businessRepository;
    private final ServiceService serviceService;
    private final ObjectMapperFactory objectMapperFactory;
    private final BusinessService businessService;
    private final ServiceFileStorageService serviceFileStorageService;

    @Autowired
    private ServiceDTOToService serviceConverter;

    @Autowired
    public ServiceController(ServiceRepository serviceRepository, BusinessRepository businessRepository, ObjectMapperFactory objectMapperFactory,  ServiceFileStorageService serviceFileStorageService) {
        this.serviceRepository = serviceRepository;
        this.businessRepository = businessRepository;
        this.serviceService = new ServiceService(serviceRepository);
        this.businessService = new BusinessService(businessRepository);
        this.objectMapperFactory = objectMapperFactory;
        this.serviceFileStorageService = serviceFileStorageService;
    }

    @GetMapping(value = "/{businessId}/services", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findAll(@PathVariable long businessId) {
            final ObjectMapper mapper = objectMapperFactory.createMapper(Service.class, new ServiceSerializer());

        try {
            return ResponseEntity.ok(mapper.writeValueAsString(serviceService.findByBusinessId(businessId)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/{businessId}/services/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findById(@PathVariable long businessId, @PathVariable long serviceId) {
        final ObjectMapper mapper = objectMapperFactory.createMapper(Service.class, new ServiceSerializer());

        try {
            final Service service = serviceService.findByIdAndBusinessId(serviceId, businessId);
            return ResponseEntity.ok(mapper.writeValueAsString(service));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @LogREST
    @PostMapping("/{businessId}/service")
    public ResponseEntity<Map<String, String>> add(@PathVariable long businessId,
                                                   @RequestBody ServiceCreateDTO serviceCreateDTO) {
        Service service = serviceConverter.convert(serviceCreateDTO);
        Business business = businessService.findById(businessId);
        service.setBusiness(business);
        return ResponseEntity.ok(serviceService.add(service));
    }

    @LogREST
    @PostMapping("/{businessId}/services/{serviceId}/profile")
    public ResponseEntity<Map<String, String>> updateProfile(@PathVariable long serviceId,
                                                             @RequestPart("file") MultipartFile serviceFile) {
        return ResponseEntity.ok(serviceFileStorageService.saveServiceFile(serviceFile, serviceId));
    }

    @LogREST
    @GetMapping("/{businessId}/services/{serviceId}/profile")
    public  ResponseEntity<Map<String,String>> getProfile(@PathVariable long serviceId) throws JSONException {
        ServiceFile serviceFile;
        try {
            serviceFile = serviceFileStorageService.getServiceFile(serviceId);
        }catch(FileStorageException e){

            // return null;
            return ResponseEntity.ok(null);

        }
        byte[] imageData = serviceFile.getData();
        String imageDataBase64Encoded = Base64.getEncoder().encodeToString(imageData);

        Map<String,String> map = new HashMap<>();
        map.put("image_encoded", imageDataBase64Encoded);

        return ResponseEntity.ok(map);

    }

    }
