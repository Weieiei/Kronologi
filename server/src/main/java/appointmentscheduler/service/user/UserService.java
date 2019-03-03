package appointmentscheduler.service.user;

import appointmentscheduler.dto.phonenumber.PhoneNumberDTO;
import appointmentscheduler.dto.settings.UpdateSettingsDTO;
import appointmentscheduler.dto.user.UpdateEmailDTO;
import appointmentscheduler.dto.user.UpdatePasswordDTO;
import appointmentscheduler.dto.user.UserLoginDTO;
import appointmentscheduler.dto.user.UserRegisterDTO;
import appointmentscheduler.entity.phonenumber.PhoneNumber;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.settings.Settings;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.entity.user.UserFactory;
import appointmentscheduler.entity.verification.Verification;
import appointmentscheduler.exception.*;
import appointmentscheduler.repository.PhoneNumberRepository;
import appointmentscheduler.repository.RoleRepository;
import appointmentscheduler.repository.SettingsRepository;
import appointmentscheduler.repository.UserRepository;
import appointmentscheduler.repository.VerificationRepository;
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
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final VerificationRepository verificationRepository;
    private final JwtProvider jwtProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SettingsRepository settingsRepository;
    private final PhoneNumberRepository phoneNumberRepository;

    @Autowired
    public UserService(
            UserRepository userRepository, RoleRepository roleRepository, JwtProvider jwtProvider,
            VerificationRepository verificationRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
            AuthenticationManager authenticationManager, SettingsRepository settingsRepository, PhoneNumberRepository phoneNumberRepository
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.verificationRepository = verificationRepository;
        this.jwtProvider = jwtProvider;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.settingsRepository = settingsRepository;
        this.phoneNumberRepository = phoneNumberRepository;
    }

    public Map<String, Object> register(UserRegisterDTO userRegisterDTO) throws IOException, MessagingException, NoSuchAlgorithmException {

        if (userRepository.findByEmailIgnoreCase(userRegisterDTO.getEmail()).orElse(null) != null) {
            throw new UserAlreadyExistsException(String.format("A user with the email %s already exists.", userRegisterDTO.getEmail()));
        }

        User user = UserFactory.createUser(User.class, userRegisterDTO.getFirstName(), userRegisterDTO.getLastName(), userRegisterDTO.getEmail(), bCryptPasswordEncoder.encode(userRegisterDTO.getPassword()));

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

        Verification verification = new Verification(savedUser);

        verificationRepository.save(verification);

        String token = generateToken(savedUser, userRegisterDTO.getPassword());

        return buildTokenRegisterMap( token, verification);
    }

    public Map<String, String> login(UserLoginDTO userLoginDTO) {
        User user = userRepository.findByEmailIgnoreCase(userLoginDTO.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Incorrect email/password combination."));

        if (!user.isVerified())
            throw new ModelValidationException("Incomplete verification");

        String token = generateToken(user, userLoginDTO.getPassword());

        return buildTokenMap(token);
    }

    private Map<String, String> buildTokenMap(String token) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);

        return map;
    }

    private Map<String, Object> buildTokenRegisterMap(String token, Verification verification) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("verification", verification);

        return map;
    }

//todo change user repository to query the businessId also, user table doesnt have businessId
    public User findUserByIdAndBusinessId(long id, long businessId) {
        User user = userRepository.findByIdAndBusinessId(id, businessId);
        if (user==null) {
            throw new UserDoesNotExistException(
                    "User id: " + id + " " +
                            "does not exist.");
        }
        else
            return user;
    }

    //TODO change the user repository to include query with businessId
    public List<User> findAllByBusinessId(long id) {
        return userRepository.findAll();
    }

    public Map<String, String> updateUser(User user) throws DataAccessException{
        userRepository.save(user);
        return message("user updated");
    }

    private String generateToken(User user, String unhashedPassword) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getId(), unhashedPassword));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(user, authentication);
    }

    public Map<String, String> updateEmail(long id, String oldEmail, UpdateEmailDTO updateEmailDTO) {

        User user = userRepository.findByIdAndEmailIgnoreCase(id, oldEmail)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with ID %d and email %s not found.", id, oldEmail)));

        if (updateEmailDTO.getPassword() == null || !bCryptPasswordEncoder.matches(updateEmailDTO.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Incorrect password.");
        }

        if (updateEmailDTO.getNewEmail() == null) {
            throw new InvalidUpdateException("You must provide a new email.");
        }

        if (user.getEmail().equalsIgnoreCase(updateEmailDTO.getNewEmail())) {
            throw new InvalidUpdateException(String.format("Your email is already %s.", user.getEmail()));
        }

        if (userRepository.findByEmailIgnoreCase(updateEmailDTO.getNewEmail()).orElse(null) != null) {
            throw new UserAlreadyExistsException(String.format("A user with the email %s already exists.", updateEmailDTO.getNewEmail()));
        }

        user.setEmail(updateEmailDTO.getNewEmail());
        userRepository.save(user);

        return message(String.format("You've successfully updated your email to %s.", user.getEmail()));

    }

    public Map<String, String> updatePassword(long id, UpdatePasswordDTO updatePasswordDTO) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with ID %d not found.", id)));

        if (updatePasswordDTO.getOldPassword() == null || !bCryptPasswordEncoder.matches(updatePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("The old password you provided is incorrect.");
        }

        if (updatePasswordDTO.getNewPassword() == null) {
            throw new PasswordNotProvidedExcetion("You must provide a new password.");
        }

        user.setPassword(bCryptPasswordEncoder.encode(updatePasswordDTO.getNewPassword()));
        userRepository.save(user);

        return message("You've successfully updated your password.");

    }

    public Settings getSettings(long userId) {
        return settingsRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Settings not found under user with ID %d.", userId)));
    }

    public Map<String, String> updateSettings(long userId, UpdateSettingsDTO updateSettingsDTO) {
        Settings settings = getSettings(userId);

        settings.setEmailReminder(updateSettingsDTO.isEmailReminder());
        settings.setTextReminder(updateSettingsDTO.isTextReminder());

        settingsRepository.save(settings);

        String message;
        boolean emailReminder = settings.isEmailReminder();
        boolean textReminder = settings.isTextReminder();

        if (!emailReminder && !textReminder) {
            message = "You will no longer receive reminders for upcoming appointments.";
        } else if (emailReminder && !textReminder) {
            message = "You will now only receive reminders for upcoming appointments via email.";
        } else if (!emailReminder && textReminder) {
            message = "You will now only receive reminders for upcoming appointments via text message.";
        } else {
            message = "You will now receive reminders for upcoming appointments via both email and text message.";
        }

        return message(message);
    }

    public PhoneNumber getPhoneNumber(long userId) {
        return phoneNumberRepository.findByUserId(userId).orElse(null);
    }

    public Map<String, String> saveOrUpdatePhoneNumber(long userId, PhoneNumberDTO phoneNumberDTO) {
        PhoneNumber phoneNumber = phoneNumberRepository.findByUserId(userId).orElse(null);
        String message;

        if (phoneNumber != null) {
            phoneNumber.setCountryCode(phoneNumberDTO.getCountryCode());
            phoneNumber.setAreaCode(phoneNumberDTO.getAreaCode());
            phoneNumber.setNumber(phoneNumberDTO.getNumber());
            message = "You've successfully updated your phone number.";
        } else {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException(String.format("User with ID %d not found.", userId)));
            phoneNumber = new PhoneNumber(phoneNumberDTO.getCountryCode(), phoneNumberDTO.getAreaCode(), phoneNumberDTO.getAreaCode(), user);
            message = "You've successfully saved your phone number.";
        }

        phoneNumberRepository.save(phoneNumber);

        return message(message);
    }

    public Map<String, String> deletePhoneNumber(long userId) {
        PhoneNumber phoneNumber = phoneNumberRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Phone number not found user used with ID %d.", userId)));

        phoneNumberRepository.delete(phoneNumber);

        return message("You've successfully deleted your phone number.");
    }

    private Map<String, String> message(String message) {
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        return map;
    }
}
