package appointmentscheduler.service.user;

import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.repository.RoleRepository;
import appointmentscheduler.repository.UserRepository;
import appointmentscheduler.util.JwtHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtHelper jwtHelper;

    @Override
    public ResponseEntity<String> register(User user) {
        user.setRoles(Stream.of(roleRepository.findByRole(RoleEnum.CLIENT)).collect(Collectors.toSet()));
        userRepository.save(user);
        return token(user);
    }

    @Override
    public ResponseEntity<String> login(User user) {
        return token(user);
    }

    private ResponseEntity<String> token(User user) {

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
