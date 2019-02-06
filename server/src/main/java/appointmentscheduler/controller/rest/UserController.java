package appointmentscheduler.controller.rest;

import appointmentscheduler.annotation.LogREST;
import appointmentscheduler.annotation.LoggingLevel;
import appointmentscheduler.dto.phonenumber.PhoneNumberDTO;
import appointmentscheduler.dto.settings.UpdateSettingsDTO;
import appointmentscheduler.dto.user.UpdateEmailDTO;
import appointmentscheduler.dto.user.UpdatePasswordDTO;
import appointmentscheduler.dto.user.UserLoginDTO;
import appointmentscheduler.dto.user.UserRegisterDTO;
import appointmentscheduler.entity.verification.Verification;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.repository.VerificationRepository;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.phonenumber.PhoneNumber;
import appointmentscheduler.entity.settings.Settings;
import appointmentscheduler.serializer.ObjectMapperFactory;
import appointmentscheduler.serializer.UserAppointmentSerializer;
import appointmentscheduler.service.appointment.AppointmentService;
import appointmentscheduler.service.email.EmailService;
import appointmentscheduler.service.user.UserService;
import appointmentscheduler.service.verification.VerificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/${rest.api.path}/user")
public class UserController extends AbstractController {

    private final UserService userService;
    private final EmailService emailService;
    private final VerificationService verificationService;

    @Autowired
    VerificationRepository verificationRepository;
    private final AppointmentService appointmentService;
    private final ObjectMapperFactory objectMapperFactory;

    @Autowired
    public UserController(UserService userService, EmailService emailService, VerificationService verificationService, AppointmentService appointmentService, ObjectMapperFactory objectMapperFactory) {
        this.userService = userService;
        this.emailService = emailService;
        this.verificationService = verificationService;
        this.appointmentService = appointmentService;
        this.objectMapperFactory = objectMapperFactory;
    }

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

    @GetMapping(value = "/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findAllAppointments() {
        final List<Appointment> appointments = appointmentService.findByClientId(getUserId());
        final ObjectMapper mapper = objectMapperFactory.createMapper(Appointment.class, new UserAppointmentSerializer());
        return getJson(mapper, appointments);
    }

    @GetMapping(value = "/appointments/{appointmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findMyAppointmentById(@PathVariable long appointmentId) {
        Appointment appointment = appointmentService.findMyAppointmentById(getUserId(), appointmentId);
        final ObjectMapper mapper = objectMapperFactory.createMapper(Appointment.class, new UserAppointmentSerializer());
        return getJson(mapper, appointment);
    }
}
