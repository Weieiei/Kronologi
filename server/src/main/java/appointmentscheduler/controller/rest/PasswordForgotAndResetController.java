package appointmentscheduler.controller.rest;

import java.util.UUID;
import javax.mail.MessagingException;
import appointmentscheduler.annotation.LogREST;
import appointmentscheduler.dto.password_reset.ResetPasswordDTO;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.entity.verification.ResetPasswordToken;
import appointmentscheduler.repository.ResetPasswordTokenRepository;
import appointmentscheduler.service.email.EmailService;
import appointmentscheduler.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/${rest.api.path}/password")
public class PasswordForgotAndResetController {

    @Autowired private UserService userService;
    @Autowired private EmailService emailService;
    @Autowired private ResetPasswordTokenRepository resetPasswordTokenRepository;
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    @LogREST
    @PostMapping(value = "/forgot")
    public ResponseEntity resetPassword(@RequestParam("email") String email) throws MessagingException {
        User user = userService.findUserByEmail(email);
        String token = UUID.randomUUID().toString();
        userService.createResetPasswordTokenForUser(user, token);
        emailService.sendPasswordResetEmail(email, token, true);

        return ResponseEntity.ok().build();
    }

    @LogREST
    @PostMapping(value="/reset")
    public ResponseEntity handleRestPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {

        ResetPasswordToken token = resetPasswordTokenRepository.findByToken(resetPasswordDTO.getToken());
        User user = token.getUser();
        String updatedPassword = passwordEncoder.encode(resetPasswordDTO.getPassword());

        if (!token.isExpired()) {
            userService.resetPassword(user.getId(), updatedPassword);
            resetPasswordTokenRepository.delete(token);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.status(401).build();
        }
    }
}

