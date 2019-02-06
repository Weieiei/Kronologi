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
import appointmentscheduler.entity.phonenumber.PhoneNumber;
import appointmentscheduler.entity.settings.Settings;
import appointmentscheduler.service.appointment.AppointmentService;
import appointmentscheduler.service.email.EmailService;
import appointmentscheduler.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/${rest.api.path}/user")
public class UserController extends AbstractController {

    private final UserService userService;
    private final EmailService emailService;
    private final AppointmentService appointmentService;

    @Autowired
    public UserController(UserService userService, EmailService emailService, AppointmentService appointmentService) {
        this.userService = userService;
        this.emailService = emailService;
        this.appointmentService = appointmentService;
    }

    @Autowired
    private CancelledDTOToCancelled cancelledAppointmentConverted;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody UserRegisterDTO userRegisterDTO) throws IOException, MessagingException {
        try {
            Map<String, Object> userTokenMap = userService.register(userRegisterDTO);
            emailService.sendEmail(userRegisterDTO.getEmail(), "ASApp Registration Confirmation", "Welcome to ASApp.<br />", true);
            return ResponseEntity.ok(userTokenMap);
        } catch (BadCredentialsException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @LogREST(LoggingLevel.WARN)
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserLoginDTO userLoginDTO) {
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

    @GetMapping("/appointments")
    public List<Appointment> findByCurrentUser() {
        return appointmentService.findByClientId(getUserId());
    }

    @LogREST
    @DeleteMapping("/appointments/{id}")
    public ResponseEntity delete( @RequestBody CancelAppointmentDTO cancel) {

        cancel.setIdPersonWhoCancelled(getUserId());
        CancelledAppointment cancelled = cancelledAppointmentConverted.convert(cancel);
        return appointmentService.cancel(cancelled);
    }
}
