package appointmentscheduler.controller.rest;


import appointmentscheduler.annotation.LogREST;
import appointmentscheduler.converters.business.BusinessHoursDTOToBusinessHours;
import appointmentscheduler.converters.service.ServiceDTOToService;
import appointmentscheduler.dto.business.BusinessDTO;
import appointmentscheduler.dto.business.BusinessHoursDTO;
import appointmentscheduler.dto.service.ServiceCreateDTO;
import appointmentscheduler.dto.user.UserRegisterDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.converters.business.BusinessDTOToBusiness;
import appointmentscheduler.entity.business.BusinessHours;
import appointmentscheduler.entity.file.File;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.entity.verification.Verification;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.repository.BusinessRepository;
import appointmentscheduler.serializer.BusinessSerializer;
import appointmentscheduler.serializer.ObjectMapperFactory;
import appointmentscheduler.serializer.UserAppointmentSerializer;
import appointmentscheduler.service.business.BusinessService;
import appointmentscheduler.service.email.EmailService;
import appointmentscheduler.service.file.FileStorageService;
import appointmentscheduler.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController
@RequestMapping("${rest.api.path}/businesses")
public class BusinessController extends AbstractController {

    private final BusinessRepository businessRepository;
    private final ObjectMapperFactory objectMapperFactory;
    private final BusinessService businessService;
    private final ModelMapper modelMapper;
    private final BusinessDTOToBusiness businessConverter;
    private ServiceDTOToService serviceConverter;
    private UserService userService;
    private EmailService emailService;
    private FileStorageService fileStorageService;
    private BusinessHoursDTOToBusinessHours businessHoursConverter;
    @Autowired
    public BusinessController(BusinessService businessService, BusinessRepository businessRepository,
                              ObjectMapperFactory objectMapperFactory, ModelMapper modelMapper,BusinessDTOToBusiness businessConverter,
                              ServiceDTOToService serviceConverter, UserService userService, EmailService emailService, FileStorageService fileStorageService,BusinessHoursDTOToBusinessHours businessHoursConverter) {

        this.businessService = businessService;
        this.businessRepository = businessRepository;
        this.objectMapperFactory = objectMapperFactory;
        this.modelMapper = modelMapper;
        this.businessConverter = businessConverter;
        this.serviceConverter = serviceConverter;
        this.businessHoursConverter = businessHoursConverter;
        this.userService = userService;
        this.emailService = emailService;
        this.fileStorageService = fileStorageService;
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


    @LogREST

    @GetMapping("/")
    public ResponseEntity<String> findAll() {
        final ObjectMapper mapper = objectMapperFactory.createMapper(Business.class, new BusinessSerializer());
        return getJson(mapper, businessRepository.findAll());
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

  /*  return ResponseEntity<Map<String, String>> response
  @PostMapping("/business")
    public ResponseEntity<Map<String, String>> add(@RequestBody BusinessDTO businessDTO) {
        Business business = businessConverter.convert(businessDTO);
        Map<String, String> message =businessService.add(business);

        return ResponseEntity.ok(message);
    }
    */
  @LogREST
  @PostMapping("/business")
  public ResponseEntity<Map<String, Object>>  add(@RequestPart("file") MultipartFile aFile, @RequestPart("business") BusinessDTO businessDTO,
                                                  @RequestPart("businessHour")BusinessHoursDTO businessHoursDTO[], @RequestPart("service") ServiceCreateDTO service, @RequestPart("user") UserRegisterDTO userRegisterDTO) throws IOException, MessagingException, NoSuchAlgorithmException {
      try {
          Map<String, Object> tokenMap = userService.register(userRegisterDTO, RoleEnum.ADMIN);
          Verification verification = (Verification) tokenMap.get("verification");
          emailService.sendRegistrationEmail(userRegisterDTO.getEmail(), verification.getHash(), true);

          //create business and save it.
          Business business = businessConverter.convert(businessDTO);
          business.setOwner(verification.getUser());
          long businessId = businessService.add(business);


          //businessHours
          List<BusinessHours> businessHours = new ArrayList<>();
          for(BusinessHoursDTO bhDTO : businessHoursDTO){
              BusinessHours businessHour = businessHoursConverter.convert(bhDTO);
              businessHour.setBusiness(business);
              businessHours.add(businessHour);
          }

          businessService.addAll(businessHours);
          //associate service to business
          Service newService = serviceConverter.convert(service);
          newService.setBusiness(business);

          fileStorageService.saveFile(aFile, businessId);
          return ResponseEntity.ok(tokenMap);
      } catch (BadCredentialsException e) {
          e.printStackTrace();
          return ResponseEntity.status(HttpStatus.CONFLICT).build();
      }
  }
}
