package appointmentscheduler.service.user;

import appointmentscheduler.dto.phonenumber.PhoneNumberDTO;
import appointmentscheduler.dto.settings.UpdateSettingsDTO;
import appointmentscheduler.dto.user.UpdateEmailDTO;
import appointmentscheduler.dto.user.UpdatePasswordDTO;
import appointmentscheduler.dto.user.UserLoginDTO;
import appointmentscheduler.dto.user.UserRegisterDTO;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.phonenumber.PhoneNumber;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.settings.Settings;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.entity.user.UserFactory;
import appointmentscheduler.entity.verification.Verification;
import appointmentscheduler.exception.*;
import appointmentscheduler.repository.*;
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
import javax.management.relation.Role;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final VerificationRepository verificationRepository;
    private final JwtProvider jwtProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SettingsRepository settingsRepository;
    private final PhoneNumberRepository phoneNumberRepository;
    private final BusinessRepository businessRepository;

    @Autowired
    public UserService(
            EmployeeRepository employeeRepository, BusinessRepository businessRepository, UserRepository userRepository,
            JwtProvider jwtProvider,
            VerificationRepository verificationRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
            AuthenticationManager authenticationManager, SettingsRepository settingsRepository, PhoneNumberRepository phoneNumberRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.businessRepository = businessRepository;
        this.userRepository = userRepository;
        this.verificationRepository = verificationRepository;
        this.jwtProvider = jwtProvider;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.settingsRepository = settingsRepository;
        this.phoneNumberRepository = phoneNumberRepository;
    }

    public Map<String, Object> register(UserRegisterDTO userRegisterDTO, RoleEnum role) throws IOException, MessagingException, NoSuchAlgorithmException {

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
        if(role.toString().equals(RoleEnum.ADMIN.toString())){
            user.setRole(RoleEnum.ADMIN.toString());
        }else{
            user.setRole(RoleEnum.CLIENT.toString());
        }

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

    public User findUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with id %d not found.", id)));
    }

//todo change user repository to query the businessId also, user table doesnt have businessId
    public User findUserByIdAndBusinessId(long id, long businessId) {
        User user = userRepository.findByIdAndBusinessId(id, businessId).
                orElseThrow(() -> new ResourceNotFoundException(String.format("User with id %d and business id %d " +
                        "not found.", id, businessId)));
        return user;
    }

    public Employee findByIdAndBusinessId(long id, long businessId) {
        Employee employee = employeeRepository.findByIdAndBusinessId(id, businessId).
                orElseThrow(() -> new ResourceNotFoundException(String.format("Employee with id %d and business id %d" +
                        " " +
                        "not found.", id, businessId)));
        return employee;
    }


    public List<User> findAllByBusinessId(long id) {
        return userRepository.findAllByBusinessId(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Users for business ID %d " +
                        "not found.", id)));
    }

    public Map<String, String> updateUser(User user, long businessId) throws DataAccessException {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Business with ID %d not found.",
                        businessId)));
        user.setBusiness(business);
        userRepository.save(user);
        return message("User updated");
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

    public Map<String, Object> business_register(UserRegisterDTO userRegisterDTO, Business business) throws IOException, MessagingException, NoSuchAlgorithmException {

        if (userRepository.findByEmailIgnoreCase(userRegisterDTO.getEmail()).orElse(null) != null) {
            throw new UserAlreadyExistsException(String.format("A user with the email %s already exists.", userRegisterDTO.getEmail()));
        }

        User user = UserFactory.createAdmin(business, User.class, userRegisterDTO.getFirstName(), userRegisterDTO.getLastName(), userRegisterDTO.getEmail(), bCryptPasswordEncoder.encode(userRegisterDTO.getPassword()));
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

         user.setRole(RoleEnum.ADMIN.toString());
        User savedUser = userRepository.save(user);

        Verification verification = new Verification(savedUser,business);

        verificationRepository.save(verification);

        String token = generateToken(savedUser, userRegisterDTO.getPassword());

        return buildTokenRegisterMap( token, verification);
    }

public Map<String, Object> business_register_test(UserRepository userRepository,UserRegisterDTO userRegisterDTO, Business business, User user, Verification verification,User savedUser, Verification savedVerification) throws IOException, MessagingException, NoSuchAlgorithmException {
//works the same as the business_register, just put the class this method depends as
//parameters, so it is easy to mock
        if (userRepository.findByEmailIgnoreCase(userRegisterDTO.getEmail()).orElse(null) != null) {
            throw new UserAlreadyExistsException(String.format("A user with the email %s already exists.", userRegisterDTO.getEmail()));
        }

         user = UserFactory.createAdmin(business, User.class, userRegisterDTO.getFirstName(), userRegisterDTO.getLastName(), userRegisterDTO.getEmail(), bCryptPasswordEncoder.encode(userRegisterDTO.getPassword()));
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

         user.setRole(RoleEnum.ADMIN.toString());
         savedUser = userRepository.save(user);

         verification = new Verification(savedUser,business);

        savedVerification = verificationRepository.save(verification);

        String token = generateToken(savedUser, userRegisterDTO.getPassword());

        return buildTokenRegisterMap( token, verification);
    }
}
