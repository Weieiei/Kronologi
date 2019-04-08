package appointmentscheduler.controller.rest;

import appointmentscheduler.dto.user.GuestDTO;
import appointmentscheduler.service.appointment.AppointmentService;
import appointmentscheduler.service.email.EmailService;
import appointmentscheduler.service.guest.GuestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
@RequestMapping("${rest.api.path}/guest")
public class GuestController extends AbstractController{

    private final GuestService guestService;
    private final EmailService emailService;

    @Autowired
    private final AppointmentService appointmentService;

    public GuestController(GuestService guestService, EmailService emailService, AppointmentService appointmentService){
        this.guestService = guestService;
        this.emailService = emailService;
        this.appointmentService = appointmentService;
    }


    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addGuest (@RequestBody GuestDTO guestDTO) throws NoSuchAlgorithmException, MessagingException, IOException {
        Map<String, Object> tokenMap = guestService.register(guestDTO);
        return ResponseEntity.ok(tokenMap);
    }

//    @PostMapping("/add")
//    public ResponseEntity<Map<String, Object>> addGuest (@RequestBody GuestDTO guestDTO){
//        Map<String, Object> tokenMap = userService.createGuest(guestDTO);
//        return ResponseEntity.ok(tokenMap);
//
//    }


}
