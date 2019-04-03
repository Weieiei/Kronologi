package appointmentscheduler.controller.rest;

import appointmentscheduler.annotation.LogREST;
import appointmentscheduler.annotation.LoggingLevel;
import appointmentscheduler.converters.appointment.CancelledDTOToCancelled;
import appointmentscheduler.dto.appointment.CancelAppointmentDTO;
import appointmentscheduler.dto.phonenumber.PhoneNumberDTO;
import appointmentscheduler.dto.settings.UpdateSettingsDTO;
import appointmentscheduler.dto.user.UpdateEmailDTO;
import appointmentscheduler.dto.user.UpdatePasswordDTO;
import appointmentscheduler.dto.user.UserLoginDTO;
import appointmentscheduler.dto.user.UserRegisterDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.appointment.CancelledAppointment;
import appointmentscheduler.entity.file.File;
import appointmentscheduler.entity.file.UserFile;
import appointmentscheduler.entity.phonenumber.PhoneNumber;
import appointmentscheduler.entity.settings.Settings;
import appointmentscheduler.entity.verification.GoogleCred;
import appointmentscheduler.entity.verification.Verification;
import appointmentscheduler.exception.FileStorageException;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.repository.GoogleCredentialRepository;
import appointmentscheduler.repository.BusinessRepository;
import appointmentscheduler.repository.VerificationRepository;
import appointmentscheduler.serializer.ObjectMapperFactory;
import appointmentscheduler.serializer.UserAppointmentSerializer;
import appointmentscheduler.service.appointment.AppointmentService;
import appointmentscheduler.service.email.EmailService;
import appointmentscheduler.service.service.ServiceService;
import appointmentscheduler.service.user.UserService;
import appointmentscheduler.service.verification.VerificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.service.business.BusinessService;
import appointmentscheduler.service.file.UserFileStorageService;
import appointmentscheduler.repository.BusinessRepository;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("${rest.api.path}/user")
public class UserController extends AbstractController {

    private final UserService userService;
    private final EmailService emailService;
    private final VerificationService verificationService;
    private final BusinessRepository businessRepository;
    private final BusinessService businessService;
    private final UserFileStorageService userFileStorageService;

    GoogleCredentialRepository repo;

    @Autowired
    VerificationRepository verificationRepository;
    private final AppointmentService appointmentService;
    private final ObjectMapperFactory objectMapperFactory;

    @Autowired
    public UserController(UserService userService, EmailService emailService, VerificationService verificationService, AppointmentService appointmentService, ObjectMapperFactory objectMapperFactory,
                          GoogleCredentialRepository repo, BusinessRepository businessRepository,UserFileStorageService userFileStorageService) {
        this.userService = userService;
        this.appointmentService = appointmentService;
        this.emailService = emailService;
        this.verificationService = verificationService;
        this.objectMapperFactory = objectMapperFactory;
        this.businessRepository = businessRepository;
        this.businessService = new BusinessService(businessRepository);
        this.userFileStorageService = userFileStorageService;
        this.repo = repo;
    }

    @Autowired
    private CancelledDTOToCancelled cancelledAppointmentConverted;

    @GetMapping("/verification")
    public ResponseEntity verify(@RequestParam(name = "hash") String hash) {
        try {
            verificationService.verify(hash);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody UserRegisterDTO userRegisterDTO) throws IOException, MessagingException, NoSuchAlgorithmException {
        try {
            Map<String, Object> tokenMap = userService.register(userRegisterDTO);
            Verification verification = (Verification) tokenMap.get("verification");
            emailService.sendRegistrationEmail(userRegisterDTO.getEmail(),verification.getHash(), true);
            return ResponseEntity.ok(tokenMap);
        } catch (BadCredentialsException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    @PostMapping("/{businessId}/business_register")
    public ResponseEntity<Map<String, Object>> business_register(@PathVariable long businessId, @RequestBody UserRegisterDTO userRegisterDTO) throws IOException, MessagingException, NoSuchAlgorithmException {
        try {
            Business business = businessService.findById(businessId);

            Map<String, Object> tokenMap = userService.business_register(userRegisterDTO,business);
            Verification verification = (Verification) tokenMap.get("verification");
            emailService.sendRegistrationEmail(userRegisterDTO.getEmail(),verification.getHash(), true);
            return ResponseEntity.ok(tokenMap);
        } catch (BadCredentialsException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    @LogREST(LoggingLevel.WARN)
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserLoginDTO userLoginDTO) {
        try {
            return ResponseEntity.ok(userService.login(userLoginDTO));
        } catch (BadCredentialsException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/email")
    public ResponseEntity<Map<String, String>> updateEmail(@RequestBody UpdateEmailDTO updateEmailDTO) {
        return ResponseEntity.ok(userService.updateEmail(getUserId(), getUserEmail(), updateEmailDTO));
    }

    @PostMapping("/password")
    public ResponseEntity<Map<String, String>> updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO) {
        return ResponseEntity.ok(userService.updatePassword(getUserId(), updatePasswordDTO));
    }

    @GetMapping("/settings")
    public Settings getSettings(@RequestAttribute long userId) {
        return userService.getSettings(userId);
    }

    @PostMapping("/settings")
    public ResponseEntity<Map<String, String>> updateSettings(@RequestBody UpdateSettingsDTO updateSettingsDTO) {
        return ResponseEntity.ok(userService.updateSettings(getUserId(), updateSettingsDTO));
    }

    @PostMapping("/profile")
    public ResponseEntity<Map<String, String>> updateProfile(@RequestPart("file") MultipartFile userFile) {
        return ResponseEntity.ok(userFileStorageService.saveUserFile(userFile, getUserId()));
    }

    @GetMapping("/profile")
    public  ResponseEntity<Map<String,String>> getProfile(@RequestAttribute long userId) throws JSONException {
        UserFile userFile;
       try {
           userFile = userFileStorageService.getUserFile(userId);
       }catch(FileStorageException e){

               // return null;
               return ResponseEntity.ok(null);

       }


        byte[] imageData = userFile.getData();
        String imageDataBase64Encoded = Base64.getEncoder().encodeToString(imageData);

        Map<String,String> map = new HashMap<>();
        map.put("image_encoded", imageDataBase64Encoded);

            return ResponseEntity.ok(map);

    }

    @GetMapping("/phone")
    public PhoneNumber getPhoneNumber(@RequestAttribute long userId) {
        return userService.getPhoneNumber(userId);
    }

    @PostMapping("/phone")
    public ResponseEntity<Map<String, String>> saveOrUpdatePhoneNumber(@RequestBody PhoneNumberDTO phoneNumberDTO) {
        return ResponseEntity.ok(userService.saveOrUpdatePhoneNumber(getUserId(), phoneNumberDTO));
    }

    @DeleteMapping("/phone")
    public ResponseEntity<Map<String, String>> deletePhoneNumber() {
        return ResponseEntity.ok(userService.deletePhoneNumber(getUserId()));
    }

    @GetMapping(value = "/business/{businessId}/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findAllAppointments(@PathVariable long businessId) {
        final List<Appointment> appointments = appointmentService.findByClientIdAndBusinessId(getUserId(), businessId);
        final ObjectMapper mapper = objectMapperFactory.createMapper(Appointment.class, new UserAppointmentSerializer());
        return getJson(mapper, appointments);
    }

    @GetMapping(value = "/business/{businessId}/appointment/{appointmentId}", produces =
            MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findMyAppointmentById(@PathVariable long appointmentId,
                                                        @PathVariable long businessId) {
        Appointment appointment = appointmentService.findMyAppointmentByIdAndBusinessId(getUserId(), appointmentId,
                businessId);
        final ObjectMapper mapper = objectMapperFactory.createMapper(Appointment.class, new UserAppointmentSerializer());
        return getJson(mapper, appointment);
    }

    @LogREST
    @PostMapping("/business/{businessId}/appointment/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable long businessId, @PathVariable long id,
                                                      @RequestBody CancelAppointmentDTO cancel) {
        cancel.setIdPersonWhoCancelled(getUserId());
        cancel.setBusinessId(businessId);
        cancel.setIdOfCancelledAppointment(id);
        cancel.setCancelReason(cancel.getCancelReason());
        CancelledAppointment cancelled = cancelledAppointmentConverted.convert(cancel);
        return ResponseEntity.ok(appointmentService.cancel(cancelled));
    }

    @LogREST
    @GetMapping(value = "/unlinkAccount")
    public ResponseEntity unlinkGoogleAccount(HttpServletResponse request) throws Exception{

        GoogleCred cred = repo.findByKey(String.valueOf(getUserId())).get();


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://accounts.google.com/o/oauth2/revoke")
                .queryParam("token", cred.getAccessToken());

        HttpEntity<?> entity = new HttpEntity<>(headers);

        HttpEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class);

        repo.delete(cred);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
