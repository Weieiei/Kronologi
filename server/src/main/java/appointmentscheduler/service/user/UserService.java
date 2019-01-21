package appointmentscheduler.service.user;

import appointmentscheduler.dto.user.UserLoginDTO;
import appointmentscheduler.dto.user.UserRegisterDTO;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.exception.UserAlreadyExistsException;
import appointmentscheduler.exception.UserDoesNotExistException;
import appointmentscheduler.repository.RoleRepository;
import appointmentscheduler.repository.UserRepository;
import appointmentscheduler.util.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtProvider jwtProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserService(
            UserRepository userRepository, RoleRepository roleRepository, JwtProvider jwtProvider,
            BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtProvider = jwtProvider;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public Map<String, Object> register(UserRegisterDTO userRegisterDTO) throws IOException, MessagingException {

        if (userRepository.findByEmail(userRegisterDTO.getEmail()).orElse(null) != null) {
            throw new UserAlreadyExistsException(String.format("An account under %s already exists.", userRegisterDTO.getEmail()));
        }

        User user = new User(
                userRegisterDTO.getFirstName(), userRegisterDTO.getLastName(),
                userRegisterDTO.getEmail(), bCryptPasswordEncoder.encode(userRegisterDTO.getPassword())
        );

        user.setRoles(Stream.of(roleRepository.findByRole(RoleEnum.CLIENT)).collect(Collectors.toSet()));

        User savedUser = userRepository.save(user);

        String token = generateToken(savedUser, userRegisterDTO.getPassword());

        return buildUserTokenMap(savedUser, token);
    }

    public Map<String, Object> login(UserLoginDTO userLoginDTO) {
        User user = userRepository.findByEmail(userLoginDTO.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Incorrect email/password combination."));

        String token = generateToken(user, userLoginDTO.getPassword());

        return buildUserTokenMap(user, token);
    }

    public User findUserByid(long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserDoesNotExistException("User id: " + id + " does not exist."));
    }

    public boolean updateUser(User user) {
       try {
           userRepository.save(user);
           return true;
       }
       catch (DataAccessException e) {
           logger.info("ERROR WHILE UPDATING USER: " + user.getId() + " ROLE TO EMPLOYEE");
           return false;

       }

    }

    private Map<String, Object> buildUserTokenMap(User user, String token) {
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("token", token);

        return map;
    }

    private String generateToken(User user, String unhashedPassword) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getId(), unhashedPassword));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(user, authentication);
    }
}
