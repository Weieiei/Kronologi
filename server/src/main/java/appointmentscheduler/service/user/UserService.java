package appointmentscheduler.service.user;

import appointmentscheduler.dto.phonenumber.PhoneNumberDTO;
import appointmentscheduler.dto.user.NewEmailDTO;
import appointmentscheduler.dto.user.NewPasswordDTO;
import appointmentscheduler.dto.user.UserLoginDTO;
import appointmentscheduler.dto.user.UserRegisterDTO;
import appointmentscheduler.entity.phonenumber.PhoneNumber;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.exception.IncorrectPasswordException;
import appointmentscheduler.exception.InvalidUpdateException;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.exception.UserAlreadyExistsException;
import appointmentscheduler.repository.RoleRepository;
import appointmentscheduler.repository.UserRepository;
import appointmentscheduler.util.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

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
            throw new UserAlreadyExistsException(String.format("A user with the email %s already exists.", userRegisterDTO.getEmail()));
        }

        User user = new User(
                userRegisterDTO.getFirstName(), userRegisterDTO.getLastName(),
                userRegisterDTO.getEmail(), bCryptPasswordEncoder.encode(userRegisterDTO.getPassword())
        );

        if (userRegisterDTO.getPhoneNumber() != null) {

            PhoneNumberDTO phoneNumberDTO = userRegisterDTO.getPhoneNumber();
            PhoneNumber phoneNumber = new PhoneNumber(
                    phoneNumberDTO.getCountryCode(),
                    phoneNumberDTO.getAreaCode(),
                    phoneNumberDTO.getNumber()
            );

            user.setPhoneNumber(phoneNumber);
            phoneNumber.setUser(user);

        }

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

    public void updateEmail(long id, String oldEmail, NewEmailDTO newEmailDTO) {

        User user = userRepository.findByIdAndEmail(id, oldEmail)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with ID %d and email %s not found.", id, oldEmail)));

        if (!bCryptPasswordEncoder.matches(newEmailDTO.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Incorrect password.");
        }

        if (oldEmail.equalsIgnoreCase(newEmailDTO.getNewEmail())) {
            throw new InvalidUpdateException(String.format("Your email is already %s.", oldEmail));
        }

        if (userRepository.findByEmail(newEmailDTO.getNewEmail()).orElse(null) != null) {
            throw new UserAlreadyExistsException(String.format("A user with the email %s already exists.", newEmailDTO.getNewEmail()));
        }

        user.setEmail(newEmailDTO.getNewEmail());
        userRepository.save(user);

    }

    public void updatePassword(long id, NewPasswordDTO newPasswordDTO) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with ID %d not found.", id)));

        if (!bCryptPasswordEncoder.matches(newPasswordDTO.getOldPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("The old password you provided is incorrect.");
        }

        user.setPassword(bCryptPasswordEncoder.encode(newPasswordDTO.getNewPassword()));
        userRepository.save(user);

    }
}
