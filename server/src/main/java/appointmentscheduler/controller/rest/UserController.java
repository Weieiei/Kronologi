package appointmentscheduler.controller.rest;

import appointmentscheduler.dto.user.UserLoginDTO;
import appointmentscheduler.dto.user.UserRegisterDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.service.AuthenticationService;
import appointmentscheduler.service.appointment.AppointmentService;
import appointmentscheduler.service.email.EmailService;
import appointmentscheduler.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/${rest.api.path}/user")
public class UserController {

    private final UserService userService;

    private final AppointmentService appointmentService;
    private final AuthenticationService authenticationService;
    private final EmailService emailService;
    @Autowired
    public UserController(UserService userService, AppointmentService appointmentService, AuthenticationService authenticationService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
        this.appointmentService = appointmentService;
        this.authenticationService = authenticationService;

    }

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

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserLoginDTO userLoginDTO) {
        try {
            return ResponseEntity.ok(userService.login(userLoginDTO));
        } catch (BadCredentialsException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/current/appointments")
    public List<Appointment> findByCurrentUser() {
        return appointmentService.findByClientId(authenticationService.getCurrentUserId());
    }

}
