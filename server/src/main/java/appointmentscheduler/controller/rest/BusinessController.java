package appointmentscheduler.controller.rest;


import appointmentscheduler.annotation.LogREST;
import appointmentscheduler.converters.business.BusinessDTOToBusiness;
import appointmentscheduler.converters.business.BusinessHoursDTOToBusinessHours;
import appointmentscheduler.converters.service.ServiceDTOToService;
import appointmentscheduler.dto.business.BusinessDTO;
import appointmentscheduler.dto.business.BusinessHoursDTO;
import appointmentscheduler.dto.service.ServiceCreateDTO;
import appointmentscheduler.dto.user.UserRegisterDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.business.BusinessHours;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.verification.Verification;
import appointmentscheduler.repository.BusinessRepository;
import appointmentscheduler.serializer.BusinessSerializer;
import appointmentscheduler.serializer.ObjectMapperFactory;
import appointmentscheduler.serializer.UserAppointmentSerializer;
import appointmentscheduler.service.GoogleApiCalls.GoogleApi;
import appointmentscheduler.service.business.BusinessService;
import appointmentscheduler.service.email.EmailService;
import appointmentscheduler.service.file.FileStorageService;
import appointmentscheduler.service.service.ServiceService;
import appointmentscheduler.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.errors.ApiException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("${rest.api.path}/businesses")
public class BusinessController extends AbstractController {

    @Value("${google.key}")
    private String googleApiKey;

    private final String inDepthPlacesAPIurl = "https://maps.googleapis.com/maps/api/place/details/json";

    private final BusinessRepository businessRepository;
    private final ObjectMapperFactory objectMapperFactory;
    private final BusinessService businessService;
    private final ModelMapper modelMapper;
    private final BusinessDTOToBusiness businessConverter;
    private GoogleApi googleApi;
    private ServiceService serviceService;
    private ServiceDTOToService serviceConverter;
    private UserService userService;
    private EmailService emailService;
    private FileStorageService fileStorageService;
    private BusinessHoursDTOToBusinessHours businessHoursConverter;
    @Autowired
    public BusinessController(  BusinessService businessService, BusinessRepository businessRepository,
                                ObjectMapperFactory objectMapperFactory, ModelMapper modelMapper,BusinessDTOToBusiness businessConverter,
                                ServiceService serviceService, ServiceDTOToService serviceConverter, UserService userService, EmailService emailService,
                                FileStorageService fileStorageService,BusinessHoursDTOToBusinessHours businessHoursConverter, GoogleApi googleApi) {
        this.googleApi = googleApi;
        this.businessService = businessService;
        this.businessRepository = businessRepository;
        this.objectMapperFactory = objectMapperFactory;
        this.modelMapper = modelMapper;
        this.businessConverter = businessConverter;
        this.serviceService = serviceService;
        this.serviceConverter = serviceConverter;
        this.businessHoursConverter = businessHoursConverter;
        this.userService = userService;
        this.emailService = emailService;
        this.fileStorageService = fileStorageService;
    }

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


  @LogREST
  @GetMapping("/getMoreInfo")
  public ResponseEntity<Map<String,List<String>>> findWithGoogle(@RequestParam(required = false) String nameOfBusiness, @RequestParam(required = false) String addressOfBusiness) throws JSONException, InterruptedException, ApiException, IOException {
      Map<String,List<String>> returnMap = new HashMap<>();
      if(addressOfBusiness.equalsIgnoreCase("undefined") && nameOfBusiness.equalsIgnoreCase("undefined")) {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }else if(nameOfBusiness.equalsIgnoreCase("undefined")){
          returnMap.put("pictures", null);
          returnMap.put("review", null);
          returnMap.put("rating", null);
          return ResponseEntity.ok(returnMap);
      }
      try{
          returnMap = this.googleApi.getMoreBusinessInfo(nameOfBusiness,addressOfBusiness,400,400);
          return ResponseEntity.ok(returnMap);
      }catch(Exception e){
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }

  }

    @LogREST
    @GetMapping("/getInfoFromBusiness")
    public ResponseEntity<String> getInfo(@RequestParam String nameOfBusiness) throws JSONException {

        List<Business> googleBusinesses = new ArrayList<>();
        final ObjectMapper mapper = objectMapperFactory.createMapper(Business.class, new BusinessSerializer());
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/place/findplacefromtext/json")
                .queryParam("key", googleApiKey)
                .queryParam("input", nameOfBusiness.replaceAll(" ",""))
                .queryParam("inputtype", "textquery")
                .queryParam("fields","place_id");

        HttpEntity<?> entity = new HttpEntity<>(headers);


        HttpEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class);

        JSONObject googlePlaceId = new JSONObject(response.getBody());
        JSONArray candidates = googlePlaceId.getJSONArray("candidates");
        List<String> candidatesIdList = new ArrayList<>();
        for(int i = 0; i < candidates.length(); i++){
            candidatesIdList.add(candidates.getJSONObject(i).getString("place_id"));
        }

        //means we found only 1 business and we'll fetch on that
        if(candidatesIdList.size() == 1){
            UriComponentsBuilder build = UriComponentsBuilder.fromHttpUrl(inDepthPlacesAPIurl)
                    .queryParam("key", googleApiKey)
                    .queryParam("placeid", candidatesIdList.get(0))
                    .queryParam("fields","opening_hours,formatted_address,name");

            entity = new HttpEntity<>(headers);

            response = restTemplate.exchange(
                    build.toUriString(),
                    HttpMethod.GET,
                    entity,
                    String.class);

            JSONObject responseJson = new JSONObject(response.getBody());
            JSONArray periodsArray = responseJson.getJSONObject("result").getJSONObject("opening_hours").getJSONArray("periods");

            List<BusinessHours> businessHoursList = new ArrayList<>();

            for(int i = 0 ; i < periodsArray.length(); i++){
                BusinessHours temp = new BusinessHours();
                temp.setDayOfWeek(periodsArray.getJSONObject(i).getJSONObject("open").getInt("day"));

                String startTimeString = periodsArray.getJSONObject(i).getJSONObject("open").getString("time");
                String endTimeString = periodsArray.getJSONObject(i).getJSONObject("close").getString("time");

                temp.setStartTime(convertMilitaryStringToRegular(startTimeString));
                temp.setEndTime(convertMilitaryStringToRegular(endTimeString));

                businessHoursList.add(temp);

            }

            Collections.sort(businessHoursList, Comparator.comparingInt(BusinessHours::getDayOfWeek));
            Business business = new Business();
            business.setOwner(null);
            business.setBusinessHours(businessHoursList);
            business.setName(responseJson.getJSONObject("result").getString("name"));
            business.setAddress(responseJson.getJSONObject("result").getString("formatted_address"));
            googleBusinesses.add(business);
            return getJson(mapper, googleBusinesses);
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

  @LogREST
  @PostMapping("/businessWithLogo")
  public ResponseEntity<Map<String, Object>> createBusinessWithLogo(@RequestPart("file") MultipartFile aFile,
          @RequestPart("business") BusinessDTO businessDTO, @RequestPart("businessHours")BusinessHoursDTO[] businessHoursDTOs,
          @RequestPart("services") ServiceCreateDTO[] serviceCreateDTOs, @RequestPart("user") UserRegisterDTO userRegisterDTO) throws IOException,
          MessagingException, NoSuchAlgorithmException {
      return createBusiness(aFile, businessDTO, businessHoursDTOs, serviceCreateDTOs, userRegisterDTO);
  }

  @LogREST
  @PostMapping("/businessNoLogo")
  public ResponseEntity<Map<String, Object>> createBusinessWithNoLogo(@RequestPart("business") BusinessDTO businessDTO,
          @RequestPart("businessHours")BusinessHoursDTO[] businessHoursDTOs, @RequestPart("services") ServiceCreateDTO[] serviceCreateDTOs,
          @RequestPart("user") UserRegisterDTO userRegisterDTO) throws IOException, MessagingException, NoSuchAlgorithmException {
      return createBusiness(null, businessDTO, businessHoursDTOs, serviceCreateDTOs, userRegisterDTO);
  }

  private ResponseEntity<Map<String, Object>> createBusiness(MultipartFile aFile, BusinessDTO businessDTO, BusinessHoursDTO[] businessHoursDTOs,
            ServiceCreateDTO[] serviceCreateDTOs, UserRegisterDTO userRegisterDTO)throws IOException, MessagingException, NoSuchAlgorithmException {
        try {

            Map<String, Object> tokenMap = userService.register(userRegisterDTO, RoleEnum.ADMIN);
            Verification verification = (Verification) tokenMap.get("verification");
            emailService.sendRegistrationEmail(userRegisterDTO.getEmail(), verification.getHash(), true);

            // create business and save it
            Business business = businessConverter.convert(businessDTO);
            business.setOwner(verification.getUser());
            long businessId = businessService.add(business);

            // create and associate business hours to business
            List<BusinessHours> businessHours = new ArrayList<>();
            for(BusinessHoursDTO bhDTO : businessHoursDTOs){
                BusinessHours businessHour = businessHoursConverter.convert(bhDTO);
                businessHour.setBusiness(business);
                businessHours.add(businessHour);
            }
            businessService.addAll(businessHours);

            // create and associate services to business
            List<Service> services = new ArrayList<>();
            for(ServiceCreateDTO scDTO : serviceCreateDTOs){
                Service service = serviceConverter.convert(scDTO);
                service.setBusiness(business);
                services.add(service);
            }
            serviceService.addAll(services);

            if(aFile !=null){
                fileStorageService.saveFile(aFile, businessId);
            }
            return ResponseEntity.ok(tokenMap);
        } catch (BadCredentialsException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

  private LocalTime convertMilitaryStringToRegular(String timeString){
      int time = Integer.parseInt(timeString);
      String value = String.format("%04d", time);
      LocalTime lt = LocalTime.parse(value, DateTimeFormatter.ofPattern("HHmm"));

      return lt;
  }

}
