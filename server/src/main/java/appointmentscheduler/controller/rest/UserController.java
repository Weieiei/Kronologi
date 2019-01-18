package appointmentscheduler.controller.rest;

import appointmentscheduler.dto.user.UserLoginDTO;
import appointmentscheduler.dto.user.UserRegisterDTO;
import appointmentscheduler.service.email.EmailService;
import appointmentscheduler.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            emailService.sendEmail(userRegisterDTO.getEmail(), "ASApp Registration Confirmation", generateRegistrationMessage(), true);
            return ResponseEntity.ok(userTokenMap);
        } catch (BadCredentialsException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    public String generateRegistrationMessage()
    {
        String message = "Welcome to ASApp! Please Confirm your email by clicking on the button below.<br />" ;
        String button = "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "  <tr>\n" +
                "      <td>\n" +
                "          <table cellspacing=\"0\" cellpadding=\"0\">\n" +
                "              <tr>\n" +
                "                  <td style=\"border-radius: 2px;\" bgcolor=\"#ED2939\">\n" +
                "                      <a href=\"https://www.copernica.com\" target=\"_blank\" style=\"padding: 8px 12px; border: 1px solid #ED2939;border-radius: 2px;font-family: Helvetica, Arial, sans-serif;font-size: 14px; color: #ffffff;text-decoration: none;font-weight:bold;display: inline-block;\">\n" +
                "                          Confirm Email             \n" +
                "                      </a>\n" +
                "                  </td>\n" +
                "              </tr>\n" +
                "          </table>\n" +
                "      </td>\n" +
                "  </tr>\n" +
                "</table>";
        return message + button;
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
