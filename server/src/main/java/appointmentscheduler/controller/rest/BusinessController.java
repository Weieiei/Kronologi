package appointmentscheduler.controller.rest;


import appointmentscheduler.annotation.LogREST;
import appointmentscheduler.converters.business.BusinessDTOToBusiness;
import appointmentscheduler.converters.business.BusinessHoursDTOToBusinessHours;
import appointmentscheduler.converters.service.ServiceDTOToService;
import appointmentscheduler.dto.business.BusinessDTO;
import appointmentscheduler.dto.business.BusinessHoursDTO;
import appointmentscheduler.dto.service.ServiceCreateDTO;
import appointmentscheduler.dto.user.BankAccountDTO;
import appointmentscheduler.dto.user.StripeSellerInfoDTO;
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

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Token;
import com.stripe.net.RequestOptions;
import com.stripe.model.Account;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("${rest.api.path}/businesses")
public class BusinessController extends AbstractController {

    @Value("${stripe.key}")
    private String stripe_key;

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
          @RequestPart("services") ServiceCreateDTO[] serviceCreateDTOs, @RequestPart("user") UserRegisterDTO userRegisterDTO, @RequestPart("stripeSellerInfo") StripeSellerInfoDTO stripeSellerInfoDTO, @RequestPart("bankAccountInfo") BankAccountDTO bankAccount) throws StripeException,IOException,
          MessagingException, NoSuchAlgorithmException {
      return createBusiness(aFile, businessDTO, businessHoursDTOs, serviceCreateDTOs, userRegisterDTO, stripeSellerInfoDTO, bankAccount);
  }

  @LogREST
  @PostMapping("/businessNoLogo")
  public ResponseEntity<Map<String, Object>> createBusinessWithNoLogo(@RequestPart("business") BusinessDTO businessDTO,
                                                                      @RequestPart("businessHours")BusinessHoursDTO[] businessHoursDTOs, @RequestPart("services") ServiceCreateDTO[] serviceCreateDTOs,
                                                                      @RequestPart("user") UserRegisterDTO userRegisterDTO, @RequestPart("stripeSellerInfo")StripeSellerInfoDTO stripeSellerInfoDTO, @RequestPart("bankAccountInfo") BankAccountDTO bankAccount) throws StripeException, IOException, MessagingException, NoSuchAlgorithmException {
      return createBusiness(null, businessDTO, businessHoursDTOs, serviceCreateDTOs, userRegisterDTO, stripeSellerInfoDTO,bankAccount);
  }

  private ResponseEntity<Map<String, Object>> createBusiness(MultipartFile aFile, BusinessDTO businessDTO, BusinessHoursDTO[] businessHoursDTOs,
            ServiceCreateDTO[] serviceCreateDTOs, UserRegisterDTO userRegisterDTO, StripeSellerInfoDTO stripeSellerInfoDTO, BankAccountDTO bankAccount)throws StripeException,IOException, MessagingException, NoSuchAlgorithmException {

        Account newBusinessStripeAccount  = null;
        if(stripeSellerInfoDTO != null){
            Stripe.apiKey =stripe_key;

            String ipAddress = this.getIpAddresss();

            String bankAccountType = bankAccount.getAccountType().equalsIgnoreCase("business") ? "company" : "individual";
            Map<String, Object> bankAccountInfo = new HashMap<String, Object>();
            bankAccountInfo.put("object","bank_account");
            bankAccountInfo.put("country", "CA");
            bankAccountInfo.put("currency","CAD");
            bankAccountInfo.put("account_holder_name", bankAccount.getBankAccountHolderFirstName() + bankAccount.getBankAccountHolderLastName());
            bankAccountInfo.put("account_holder_type", bankAccountType);
            bankAccountInfo.put("routing_number", bankAccount.getRoutinNumber());
            bankAccountInfo.put("account_number", bankAccount.getAccountNumber());

            Map<String, Object> accountParams = new HashMap<String, Object>();
            Map<String, Object> address = new HashMap<String, Object>();

            Map<String,Object> dob = new HashMap<>();
            accountParams.put("type", "custom");
            accountParams.put("country", "CA");
            accountParams.put("email", userRegisterDTO.getEmail());

            Map<String,Object> companyParams = new HashMap<>();
            Map<String,Object> individualParams = new HashMap<>();
            if(stripeSellerInfoDTO.getBusinessTaxNumber() != 0){
                companyParams.put("name",businessDTO.getName());
                companyParams.put("tax_id", stripeSellerInfoDTO.getBusinessTaxNumber());
                accountParams.put("company",companyParams);

            }else{
                address.put("city",stripeSellerInfoDTO.getCity());
                address.put("country","CA");
                address.put("line1",stripeSellerInfoDTO.getAddress());
                address.put("postal_code",stripeSellerInfoDTO.getPostalCode());
                address.put("state", "QC");

                dob.put("day", stripeSellerInfoDTO.getBirthDay());
                dob.put("month", stripeSellerInfoDTO.getBirthMonth());
                dob.put("year", stripeSellerInfoDTO.getBirthYear());
                individualParams.put("address", stripeSellerInfoDTO.getAddress());
                individualParams.put("first_name",stripeSellerInfoDTO.getFirstName());
                individualParams.put("last_name",stripeSellerInfoDTO.getLastName());
                individualParams.put("id_number",stripeSellerInfoDTO.getSocialInsuranceNumber());
                individualParams.put("dob",dob);
                individualParams.put("address",address);
                accountParams.put("individual", individualParams);
            }


            Map<String,Object> tosAcceptance = new HashMap<>();
            tosAcceptance.put("date", stripeSellerInfoDTO.getTosAcceptance().getTime()/1000);
            tosAcceptance.put("ip", ipAddress);
            accountParams.put("business_type",bankAccountType);
            accountParams.put("external_account",bankAccountInfo);
            accountParams.put("tos_acceptance", tosAcceptance);
            newBusinessStripeAccount = Account.create(accountParams);
        }

        try {

            Map<String, Object> tokenMap = userService.register(userRegisterDTO, RoleEnum.ADMIN);
            Verification verification = (Verification) tokenMap.get("verification");
            emailService.sendRegistrationEmail(userRegisterDTO.getEmail(), verification.getHash(), true);

            // create business and save it
            Business business = businessConverter.convert(businessDTO);
            if(newBusinessStripeAccount !=null){
                business.setStripeAccountId(newBusinessStripeAccount.getId());
            }
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
