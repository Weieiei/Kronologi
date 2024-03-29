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
import appointmentscheduler.entity.verification.ResetPasswordToken;
import appointmentscheduler.entity.verification.Verification;
import appointmentscheduler.exception.*;
import appointmentscheduler.repository.*;
import appointmentscheduler.util.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    //private final UserFileRepository userFileRepository;
   // private final UserFileStorageService userFileStorageService;
    @Autowired
    public UserService(
            EmployeeRepository employeeRepository, BusinessRepository businessRepository, UserRepository userRepository,
            JwtProvider jwtProvider,
            VerificationRepository verificationRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
            @Qualifier("authenticationManagerBean") AuthenticationManager authenticationManager, SettingsRepository settingsRepository,
            PhoneNumberRepository phoneNumberRepository, ResetPasswordTokenRepository resetPasswordTokenRepository
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
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
    }

    public Map<String, Object> register(UserRegisterDTO userRegisterDTO, RoleEnum role) throws IOException, MessagingException, NoSuchAlgorithmException {

        if (userRepository.findByEmailIgnoreCase(userRegisterDTO.getEmail()).orElse(null) != null) {
            throw new UserAlreadyExistsException(String.format("A user with the email %s already exists.", userRegisterDTO.getEmail()));
        }

        final User user = new User();
        user.setFirstName(userRegisterDTO.getFirstName());
        user.setLastName(userRegisterDTO.getLastName());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userRegisterDTO.getPassword()));

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

    public User findUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with email %d not found.", email)));
    }


    // todo change user repository to query the businessId also, user table doesnt have businessId
    public User findUserByIdAndBusinessId(long id, long businessId) {
        User user = userRepository.findByIdAndBusinessId(id, businessId).
                orElseThrow(() -> new ResourceNotFoundException(String.format("User with id %d and business id %d " +
                        "not found.", id, businessId)));
        return user;
    }

    public Employee findByIdAndBusinessId(long id, long businessId) {
        return employeeRepository.findByIdAndBusinessId(id, businessId).
                orElseThrow(() -> new ResourceNotFoundException(String.format("Employee with id %d and business id %d" +
                        " " +
                        "not found.", id, businessId)));
    }

    public List<User> findAllByBusinessId(long id) {
        return userRepository.findAllByBusinessId(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Users for business ID %d " +
                        "not found.", id)));
    }
    public void associateBusinessToUser(long businessId, long userId){
       Business business = this.businessRepository.findById(businessId).get();
       User user = this.userRepository.findById(userId).get();

       business.setOwner(user);
       this.businessRepository.save(business);
    }
    public Map<String, String> updateUser(User user, long businessId) throws DataAccessException {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Business with ID %d not found.",
                        businessId)));
        user.setBusiness(business);
        userRepository.save(user);
        return message("User updated");
    }

    public Map<String, String> updateUserRole(User user, long businessId, String role) throws DataAccessException {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Business with ID %d not found.",
                        businessId)));
        Employee employee = new Employee();
        employee.setBusiness(business);
        employee.setRole(role);
        employee.setFirstName(user.getFirstName());
        employee.setLastName(user.getLastName());
        employee.setPassword(user.getPassword());
        employee.setVerified(user.isVerified());
        employee.setCreatedAt(user.getCreatedAt());
        employee.setUpdatedAt(user.getUpdatedAt());
        employee.setSettings(user.getSettings());
        employee.setEmail(user.getEmail());
        userRepository.delete(user);
        employeeRepository.save(employee);
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

    public Map<String, String> resetPassword(long id, String newPassword) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with ID %d not found.", id)));

        if (newPassword == null) {
            throw new PasswordNotProvidedExcetion("You must provide a new password.");
        }

        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
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

    public Map<String, Object> businessRegister(UserRegisterDTO userRegisterDTO, Business business) throws IOException, MessagingException, NoSuchAlgorithmException {

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

        Verification verification = new Verification(savedUser);

        verificationRepository.save(verification);

        String token = generateToken(savedUser, userRegisterDTO.getPassword());

        return buildTokenRegisterMap( token, verification);
    }


    public void createResetPasswordTokenForUser(User user, String token) {
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken(token, user);
        resetPasswordTokenRepository.save(resetPasswordToken);
    }

    public List<User> findAllClients() {
        List<User> userlist = userRepository.findByRole(RoleEnum.CLIENT);
        if (userlist == null) {
            throw new ResourceNotFoundException("Clients not found");
        }
        return userlist;
    }

    public List<User> findAllUsersForBusiness(long businessId, RoleEnum roleEnum) {
        return userRepository.findByRoleOrBusinessIdOrderByRole(roleEnum, businessId).orElseThrow(() -> new ResourceNotFoundException(String.format(
                "Users not found for business with id %d .", businessId)));
    }
}
