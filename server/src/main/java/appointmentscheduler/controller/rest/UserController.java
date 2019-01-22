package appointmentscheduler.controller.rest;

import appointmentscheduler.dto.phonenumber.PhoneNumberDTO;
import appointmentscheduler.dto.user.*;
import appointmentscheduler.entity.phonenumber.PhoneNumber;
import appointmentscheduler.entity.settings.Settings;
import appointmentscheduler.service.email.EmailService;
import appointmentscheduler.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/${rest.api.path}/user")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public UserController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
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

    @PostMapping("/email")
    public void updateEmail(@RequestAttribute long userId, @RequestAttribute String email, @RequestBody NewEmailDTO newEmailDTO) {
        userService.updateEmail(userId, email, newEmailDTO);
    }

    @PostMapping("/password")
    public void updatePassword(@RequestAttribute long userId, @RequestBody NewPasswordDTO newPasswordDTO) {
        userService.updatePassword(userId, newPasswordDTO);
    }

    @GetMapping("/settings")
    public Settings getSettings(@RequestAttribute long userId) {
        return userService.getSettings(userId);
    }

    @PostMapping("/settings")
    public ResponseEntity<Map<String, String>> updateSettings(@RequestAttribute long userId, @RequestBody UpdateSettingsDTO updateSettingsDTO) {
        return ResponseEntity.ok(userService.updateSettings(userId, updateSettingsDTO));
    }

    @GetMapping("/phone")
    public PhoneNumber getPhoneNumber(@RequestAttribute long userId) {
        return userService.getPhoneNumber(userId);
    }

    @PostMapping("/phone")
    public ResponseEntity<Map<String, String>> saveOrUpdatePhoneNumber(@RequestAttribute long userId, @RequestBody PhoneNumberDTO phoneNumberDTO) {
        return ResponseEntity.ok(userService.saveOrUpdatePhoneNumber(userId, phoneNumberDTO));
    }

    @DeleteMapping("/phone")
    public ResponseEntity<Map<String, String>> deletePhoneNumber(@RequestAttribute long userId) {
        return ResponseEntity.ok(userService.deletePhoneNumber(userId));
    }
}
