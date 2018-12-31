package appointmentscheduler.controller.rest;

import appointmentscheduler.converters.user.UserLoginDTOToUser;
import appointmentscheduler.converters.user.UserRegisterDTOToUser;
import appointmentscheduler.dto.user.UserLoginDTO;
import appointmentscheduler.dto.user.UserRegisterDTO;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${rest.api.path}/user")
// todo remove annotation once global cors is working
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRegisterDTOToUser userRegisterConverter;

    @Autowired
    private UserLoginDTOToUser userLoginConverter;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        User user = userRegisterConverter.convert(userRegisterDTO);
        return userService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO userLoginDTO) {
        User user = userLoginConverter.convert(userLoginDTO);
        return userService.login(user);
    }

}
