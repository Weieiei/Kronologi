package appointmentscheduler.service.user;

import appointmentscheduler.dto.Token;
import appointmentscheduler.dto.user.UserLoginDTO;
import appointmentscheduler.dto.user.UserRegisterDTO;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.exception.UserAlreadyExistsException;
import appointmentscheduler.repository.RoleRepository;
import appointmentscheduler.repository.UserRepository;
import appointmentscheduler.util.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private JwtProvider jwtProvider;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<?> register(UserRegisterDTO userRegisterDTO) {

        if (userRepository.findByEmail(userRegisterDTO.getEmail()).orElse(null) != null) {
            throw new UserAlreadyExistsException(String.format("An account under %s already exists.", userRegisterDTO.getEmail()));
        }

        User user = new User(
                userRegisterDTO.getFirstName(), userRegisterDTO.getLastName(),
                userRegisterDTO.getEmail(), bCryptPasswordEncoder.encode(userRegisterDTO.getPassword())
        );

        user.setRoles(Stream.of(roleRepository.findByRole(RoleEnum.CLIENT)).collect(Collectors.toSet()));

        User savedUser = userRepository.save(user);

        String token = generateToken(savedUser.getId(), userRegisterDTO.getPassword());

        return ResponseEntity.ok(new Token(token));

    }

    @Override
    public ResponseEntity<?> login(UserLoginDTO userLoginDTO) {

        User user = userRepository.findByEmail(userLoginDTO.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Incorrect email/password combination."));

        String token = generateToken(user.getId(), userLoginDTO.getPassword());

        return ResponseEntity.ok(new Token(token));

    }

    private String generateToken(long userId, String password) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userId, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);

    }

}
