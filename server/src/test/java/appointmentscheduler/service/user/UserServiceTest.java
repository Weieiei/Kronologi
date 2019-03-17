package appointmentscheduler.service.user;

import appointmentscheduler.converters.business.BusinessDTOToBusiness;
import appointmentscheduler.dto.phonenumber.PhoneNumberDTO;
import appointmentscheduler.dto.service.ServiceCreateDTO;
import appointmentscheduler.dto.business.BusinessDTO;
import appointmentscheduler.dto.settings.UpdateSettingsDTO;
import appointmentscheduler.dto.user.UpdateEmailDTO;
import appointmentscheduler.dto.user.UpdatePasswordDTO;
import appointmentscheduler.dto.user.UserLoginDTO;
import appointmentscheduler.dto.user.UserRegisterDTO;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.phonenumber.PhoneNumber;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.settings.Settings;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.entity.user.UserFactory;
import appointmentscheduler.entity.verification.Verification;
import appointmentscheduler.exception.IncorrectPasswordException;
import appointmentscheduler.exception.InvalidUpdateException;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.exception.UserAlreadyExistsException;
import appointmentscheduler.repository.*;
import appointmentscheduler.util.JwtProvider;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private UserRepository userRepository;


    @Mock
    private VerificationRepository verificationRepository;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private SettingsRepository settingsRepository;

    @Mock
    private PhoneNumberRepository phoneNumberRepository;

    @Mock
    private BusinessRepository businessRepository;
    @Mock
    private EmployeeRepository employeeRepository;

    private UserService userService;

    @Before
    public void before() {
        userService = new UserService(
                employeeRepository, businessRepository, userRepository, jwtProvider, verificationRepository,
                bCryptPasswordEncoder, authenticationManager, settingsRepository, phoneNumberRepository
        );
    }

//    @Test(expected = UserAlreadyExistsException.class)
//    public void registerFailed() throws IOException, MessagingException, NoSuchAlgorithmException {
//        // create mocks
//        final User mockedUser = mock(User.class);
//        final UserRegisterDTO userRegisterDTO = mock(UserRegisterDTO.class);
//
//        // mock methods
//        when(userRegisterDTO.getEmail()).thenReturn("testEmail");
//        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(mockedUser));
//
//        // run method
//        userService.register(userRegisterDTO);
//
//        // fail if it didn't throw an error
//        fail("Exception should have been thrown");
//    }
//
//    @Test
//    @Ignore
//    public void registerSucceeded() {
//        fail("todo");
//    }
//
//    @Test(expected = BadCredentialsException.class)
//    public void loginFailed() {
//        // create mocks
//        final UserLoginDTO userLoginDTO = mock(UserLoginDTO.class);
//
//        // mock methods
//        when(userLoginDTO.getEmail()).thenReturn("testEmail");
//
//        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());
//
//        // run method
//        userService.login(userLoginDTO);
//
//        // fail if it didn't throw an error
//        fail("Exception should have been thrown");
//    }
//
//    @Test
//    public void loginSucceeded() {
//        // create mocks
//        final User mockedUser = mock(User.class);
//        final UserLoginDTO userLoginDTO = mock(UserLoginDTO.class);
//
//        // mock methods
//        when(mockedUser.getId()).thenReturn(1L);
//        when(mockedUser.isVerified()).thenReturn(true);
//
//        when(userLoginDTO.getEmail()).thenReturn("testEmail");
//        when(userLoginDTO.getPassword()).thenReturn("testPassword");
//
//        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(mockedUser));
//        when(jwtProvider.generateToken(any(), any())).thenReturn("testToken");
//
//        // run method and get result
//        Map map = userService.login(userLoginDTO);
//
//        // verify result contents for token
//        assertNotNull(map.get("token"));
//        assertEquals("testToken", map.get("token"));
//    }
//
//    @Test(expected = ResourceNotFoundException.class)
//    public void updateEmailFailUserNotFound() {
//        final UpdateEmailDTO updateEmailDTO = mock(UpdateEmailDTO.class);
//
//        when(userRepository.findByIdAndEmailIgnoreCase(anyLong(), anyString())).thenReturn(Optional.empty());
//        userService.updateEmail(1, "test", updateEmailDTO);
//
//        fail("Exception should have been thrown.");
//    }
//
//    @Test(expected = IncorrectPasswordException.class)
//    public void updateEmailFailIncorrectPassword() {
//        final User user = mock(User.class);
//        final UpdateEmailDTO updateEmailDTO = mock(UpdateEmailDTO.class);
//
//        when(userRepository.findByIdAndEmailIgnoreCase(anyLong(), anyString())).thenReturn(Optional.of(user));
//
//        String correctPassword = "password";
//        when(user.getPassword()).thenReturn(correctPassword);
//
//        String incorrectPassword = "password123";
//        when(updateEmailDTO.getPassword()).thenReturn(incorrectPassword);
//
//        when(bCryptPasswordEncoder.matches(incorrectPassword, correctPassword)).thenReturn(false);
//
//        userService.updateEmail(1, "test", updateEmailDTO);
//
//        fail("Letting a user update their email with an incorrect provided password.");
//
//    }
//
//    @Test(expected = InvalidUpdateException.class)
//    public void updateEmailFailSameEmail() {
//        final User user = mock(User.class);
//        final UpdateEmailDTO updateEmailDTO = mock(UpdateEmailDTO.class);
//
//        when(userRepository.findByIdAndEmailIgnoreCase(anyLong(), anyString())).thenReturn(Optional.of(user));
//
//        String correctPassword = "password";
//        when(user.getPassword()).thenReturn(correctPassword);
//        when(updateEmailDTO.getPassword()).thenReturn(correctPassword);
//
//        when(bCryptPasswordEncoder.matches(correctPassword, correctPassword)).thenReturn(true);
//
//        when(user.getEmail()).thenReturn("test@email.com");
//        when(updateEmailDTO.getNewEmail()).thenReturn("test@email.com");
//
//        userService.updateEmail(1, "test", updateEmailDTO);
//
//        fail("Letting a user update to an email they already have.");
//
//    }
//
//    @Test(expected = UserAlreadyExistsException.class)
//    public void updateEmailFailEmailAlreadyTaken() {
//        final User user = mock(User.class);
//        final UpdateEmailDTO updateEmailDTO = mock(UpdateEmailDTO.class);
//
//        when(userRepository.findByIdAndEmailIgnoreCase(anyLong(), anyString())).thenReturn(Optional.of(user));
//
//        String correctPassword = "password";
//        when(user.getPassword()).thenReturn(correctPassword);
//        when(updateEmailDTO.getPassword()).thenReturn(correctPassword);
//
//        when(bCryptPasswordEncoder.matches(correctPassword, correctPassword)).thenReturn(true);
//
//        when(user.getEmail()).thenReturn("test@email.com");
//        when(updateEmailDTO.getNewEmail()).thenReturn("test2@email.com");
//
//        final User anotherUser = mock(User.class);
//        when(userRepository.findByEmailIgnoreCase(updateEmailDTO.getNewEmail())).thenReturn(Optional.of(anotherUser));
//
//        userService.updateEmail(1, "test", updateEmailDTO);
//
//        fail("Letting a user update to an email taken by another user.");
//    }
//
//    @Test(expected = ResourceNotFoundException.class)
//    public void updatePasswordFailUserNotFound() {
//        final UpdatePasswordDTO updatePasswordDTO = mock(UpdatePasswordDTO.class);
//
//        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
//        userService.updatePassword(1, updatePasswordDTO);
//
//        fail("Exception should have been thrown.");
//    }
//
//    @Test(expected = IncorrectPasswordException.class)
//    public void updatePasswordFailIncorrectPassword() {
//        final User user = mock(User.class);
//        final UpdatePasswordDTO updatePasswordDTO = mock(UpdatePasswordDTO.class);
//
//        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
//
//        String correctPassword = "password";
//        when(user.getPassword()).thenReturn(correctPassword);
//
//        String incorrectPassword = "password123";
//        when(updatePasswordDTO.getOldPassword()).thenReturn(incorrectPassword);
//
//        when(bCryptPasswordEncoder.matches(incorrectPassword, correctPassword)).thenReturn(false);
//
//        userService.updatePassword(1, updatePasswordDTO);
//
//        fail("Letting a user update their password even though they provided an incorrect original password.");
//    }
//
//    @Test(expected = ResourceNotFoundException.class)
//    public void getSettingsFailSettingsDontExist() {
//        when(settingsRepository.findByUserId(1L)).thenReturn(Optional.empty());
//        userService.getSettings(1L);
//        fail("Exception should have been thrown. Could not find user settings, therefore cannot proceed.");
//    }
//
//    @Test(expected = ResourceNotFoundException.class)
//    public void updateSettingsFailSettingsDontExist() {
//        final UpdateSettingsDTO updateSettingsDTO = mock(UpdateSettingsDTO.class);
//        when(settingsRepository.findByUserId(1L)).thenReturn(Optional.empty());
//        userService.updateSettings(1L, updateSettingsDTO);
//        fail("Exception should have been thrown. Could not find user settings, therefore cannot proceed.");
//    }
//
//    @Test
//    public void updateSettingsSuccessBothRemindersTrue() {
//        final Settings mockSettings = mock(Settings.class);
//        final UpdateSettingsDTO updateSettingsDTO = mock(UpdateSettingsDTO.class);
//
//        when(settingsRepository.findByUserId(1L)).thenReturn(Optional.of(mockSettings));
//        when(mockSettings.isEmailReminder()).thenReturn(true);
//        when(mockSettings.isTextReminder()).thenReturn(true);
//
//        Map<String, String> map = userService.updateSettings(1L, updateSettingsDTO);
//        assertEquals("You will now receive reminders for upcoming appointments via both email and text message.", map.get("message"));
//    }
//
//    @Test(expected = ResourceNotFoundException.class)
//    public void updatePhoneNumberFail() {
//        final PhoneNumberDTO mockPhoneNumberDTO = mock(PhoneNumberDTO.class);
//
//        when(phoneNumberRepository.findByUserId(1L)).thenReturn(Optional.empty());
//        when(userRepository.findById(1L)).thenReturn(Optional.empty());
//
//        userService.saveOrUpdatePhoneNumber(1L, mockPhoneNumberDTO);
//
//        fail("Exception should have been thrown. Could not find user, therefore cannot proceed.");
//    }
//
//    @Test
//    public void updatePhoneNumberSuccess() {
//        final PhoneNumber mockPhoneNumber = mock(PhoneNumber.class);
//        final PhoneNumberDTO mockPhoneNumberDTO = mock(PhoneNumberDTO.class);
//
//        when(phoneNumberRepository.findByUserId(1L)).thenReturn(Optional.of(mockPhoneNumber));
//
//        Map<String, String> map = userService.saveOrUpdatePhoneNumber(1L, mockPhoneNumberDTO);
//        assertEquals("You've successfully updated your phone number.", map.get("message"));
//    }
//
//    @Test
//    public void savePhoneNumberSuccess() {
//        final User mockUser = mock(User.class);
//        final PhoneNumberDTO mockPhoneNumberDTO = mock(PhoneNumberDTO.class);
//
//        when(phoneNumberRepository.findByUserId(1L)).thenReturn(Optional.empty());
//        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
//
//        Map<String, String> map = userService.saveOrUpdatePhoneNumber(1L, mockPhoneNumberDTO);
//        assertEquals("You've successfully saved your phone number.", map.get("message"));
//    }
//
//    @Test(expected = ResourceNotFoundException.class)
//    public void deletePhoneNumberFailNotFound() {
//        when(phoneNumberRepository.findByUserId(1L)).thenReturn(Optional.empty());
//        userService.deletePhoneNumber(1L);
//        fail("Exception should have been thrown. Could not find phone number, therefore cannot proceed.");
//    }
//
//    @Test
//    public void deletePhoneNumberSuccess() {
//        PhoneNumber mockPhoneNumber = mock(PhoneNumber.class);
//
//        when(phoneNumberRepository.findByUserId(1L)).thenReturn(Optional.of(mockPhoneNumber));
//
//        Map<String, String> map = userService.deletePhoneNumber(1L);
//        assertEquals("You've successfully deleted your phone number.", map.get("message"));
//    }
@Test(expected = UserAlreadyExistsException.class)
public void businessRegisterFailed() throws IOException, MessagingException, NoSuchAlgorithmException {
    // create mocks
    final User mockedUser = mock(User.class);
    final UserRegisterDTO userRegisterDTO = mock(UserRegisterDTO.class);


    // mock methods
    when(userRegisterDTO.getEmail()).thenReturn("testEmail");
    when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(mockedUser));

    // run method
    userService.register(userRegisterDTO);

    // fail if it didn't throw an error
    fail("Exception should have been thrown");
}

    @Test
    public void businessRegisterSucceeded() throws IOException, MessagingException, NoSuchAlgorithmException{
        // create mocks
        final User mockedUser = mock(User.class);
        final User savedUser =mock(User.class);
        final UserRegisterDTO userRegisterDTO = mock(UserRegisterDTO.class);
        final Business mockedBusiness= mock(Business.class);
        final Verification mockedVerification = mock(Verification.class);
        final Verification savedVerification = mock(Verification.class);
        final VerificationRepository verificationRepository =mock(VerificationRepository.class);
        final UserRepository userRepository = mock(UserRepository.class);
        final UserFactory userFactory = mock(UserFactory.class);


        when(userRegisterDTO.getFirstName()).thenReturn("Test");
        when(userRegisterDTO.getLastName()).thenReturn("Test");
        when(userRegisterDTO.getEmail()).thenReturn("TestMail");
        when(userRegisterDTO.getPassword()).thenReturn("test1");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(verificationRepository.save(any(Verification.class))).thenReturn(savedVerification);

        //when(userFactory.createAdmin(any(Business.class),User.class,anyString(),anyString()
         //       ,anyString(),anyString())
       //).thenReturn(mockedUser);

        Map<String, Object> map = userService.business_register_test(userRepository, userRegisterDTO, mockedBusiness,mockedUser,mockedVerification ,savedUser, savedVerification);


       // verify(userFactory).createAdmin(any(Business.class),User.class,anyString(),anyString()
        //        ,anyString(),anyString());
       // verify(mockedUser).setRole(anyString());
        verify(userRepository).save(any(User.class));
       // verify(verificationRepository).save(any(Verification.class));

    }
}
