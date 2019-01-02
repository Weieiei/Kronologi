package appointmentscheduler.controller.rest;

import appointmentscheduler.dto.Token;
import appointmentscheduler.dto.user.UserLoginDTO;
import appointmentscheduler.dto.user.UserRegisterDTO;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.repository.UserRepository;
import appointmentscheduler.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${rest.api.path}/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Token register(@RequestBody UserRegisterDTO userRegisterDTO) {
        return userService.register(userRegisterDTO);
    }

    @PostMapping("/login")
    public Token login(@RequestBody UserLoginDTO userLoginDTO) {
        return userService.login(userLoginDTO);
    }
}
