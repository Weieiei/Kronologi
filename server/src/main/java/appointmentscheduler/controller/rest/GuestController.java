package appointmentscheduler.controller.rest;

import appointmentscheduler.dto.user.GuestDTO;
import appointmentscheduler.dto.user.UserRegisterDTO;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.entity.verification.Verification;
import appointmentscheduler.service.appointment.AppointmentService;
import appointmentscheduler.service.email.EmailService;
import appointmentscheduler.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${rest.api.path}/guest")
public class GuestController extends AbstractController{

    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    private final AppointmentService appointmentService;

    public GuestController(UserService userService, EmailService emailService, AppointmentService appointmentService){
        this.userService = userService;
        this.emailService = emailService;
        this.appointmentService = appointmentService;
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> addGuest (@RequestBody GuestDTO guestDTO, User guest, UserRegisterDTO userRegisterDTO){
        Map<String, String> tokenMap = userService.createGuest(guestDTO);
        return ResponseEntity.ok(tokenMap);

    }

}
