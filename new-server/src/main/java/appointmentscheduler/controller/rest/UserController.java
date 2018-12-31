package appointmentscheduler.controller.rest;

import appointmentscheduler.dto.UserLoginDTO;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.repository.UserRepository;
import appointmentscheduler.util.JwtHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${rest.api.path}/user")
// todo remove annotation once global cors is working
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class UserController {

    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, JwtHelper jwtHelper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.jwtHelper = jwtHelper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO userLoginDTO) {
        // find user by email
        final User user = userRepository.findUserByEmail(userLoginDTO.getEmail());

        if (user != null) {
            // check password
            boolean doesMatch = bCryptPasswordEncoder.matches(userLoginDTO.getPassword(), user.getPassword());

            //noinspection ConstantConditions
            if (doesMatch) {
                // generate token
                try {
                    final ObjectMapper mapper = new ObjectMapper();
                    // todo return a tokenDTO instead of a user, because we don't want to store the password (and maybe other fields) in the token
                    final String token = jwtHelper.createJWT("1", "Me", mapper.writeValueAsString(user), 1000);

                    return ResponseEntity.ok("{\"token\" : \"" + token + "\"}");
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not generate token");
            }
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("forbidden");
    }
}
