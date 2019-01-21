package appointmentscheduler.controller.rest;

import appointmentscheduler.dto.user.UserLoginDTO;
import appointmentscheduler.dto.user.UserRegisterDTO;
import appointmentscheduler.entity.verification.Verification;
import appointmentscheduler.repository.VerificationRepository;
import appointmentscheduler.service.email.EmailService;
import appointmentscheduler.service.user.UserService;
import appointmentscheduler.service.verification.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
@RequestMapping("/${rest.api.path}/user")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final VerificationService verificationService;

    @Autowired
    VerificationRepository verificationRepository;

    @Autowired
    public UserController(UserService userService, EmailService emailService, VerificationService verificationService) {
        this.userService = userService;
        this.emailService = emailService;
        this.verificationService = verificationService;
    }

    @GetMapping("/verification")
    public int getAttr() {
        verificationService.verify("b642b4217b34b1e8d3bd915fc65c4452");
        return 0;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody UserRegisterDTO userRegisterDTO) throws IOException, MessagingException, NoSuchAlgorithmException {
        try {
            Map<String, Object> userTokenMap = userService.register(userRegisterDTO);
            Verification verif = (Verification) userTokenMap.get("verification");
            emailService.sendRegistrationEmail(userRegisterDTO.getEmail(),verif.getHash(), true);
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
}
