package appointmentscheduler.service.user;

import appointmentscheduler.dto.user.NewEmailDTO;
import appointmentscheduler.dto.user.NewPasswordDTO;
import appointmentscheduler.dto.user.UserLoginDTO;
import appointmentscheduler.dto.user.UserRegisterDTO;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.exception.IncorrectPasswordException;
import appointmentscheduler.exception.InvalidUpdateException;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.exception.UserAlreadyExistsException;
import appointmentscheduler.repository.PhoneNumberRepository;
import appointmentscheduler.repository.RoleRepository;
import appointmentscheduler.repository.SettingsRepository;
import appointmentscheduler.repository.UserRepository;
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
    private RoleRepository roleRepository;

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

    private UserService userService;

    @Before
    public void before() {
        userService = new UserService(
                userRepository, roleRepository, jwtProvider,
                bCryptPasswordEncoder, authenticationManager,
                settingsRepository, phoneNumberRepository
        );
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void registerFailed() throws IOException, MessagingException {
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
    @Ignore
    public void registerSucceeded() {
        fail("todo");
    }

    @Test(expected = BadCredentialsException.class)
    public void loginFailed() {
        // create mocks
        final UserLoginDTO userLoginDTO = mock(UserLoginDTO.class);

        // mock methods
        when(userLoginDTO.getEmail()).thenReturn("testEmail");

        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());

        // run method
        userService.login(userLoginDTO);

        // fail if it didn't throw an error
        fail("Exception should have been thrown");
    }

    @Test
    public void loginSucceeded() {
        // create mocks
        final User mockedUser = mock(User.class);
        final UserLoginDTO userLoginDTO = mock(UserLoginDTO.class);

        // mock methods
        when(mockedUser.getId()).thenReturn(1L);

        when(userLoginDTO.getEmail()).thenReturn("testEmail");
        when(userLoginDTO.getPassword()).thenReturn("testPassword");

        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(mockedUser));
        when(jwtProvider.generateToken(any(), any())).thenReturn("testToken");

        // run method and get result
        Map map = userService.login(userLoginDTO);

        // verify result contents for user
        assertNotNull(map.get("user"));
        assertEquals(1, ((User) map.get("user")).getId());

        // verify result contents for token
        assertNotNull(map.get("token"));
        assertEquals("testToken", map.get("token"));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateEmailFailUserNotFound() {
        final NewEmailDTO newEmailDTO = mock(NewEmailDTO.class);

        when(userRepository.findByIdAndEmailIgnoreCase(anyLong(), anyString())).thenReturn(Optional.empty());
        userService.updateEmail(1, "test", newEmailDTO);

        fail("Exception should have been thrown.");
    }

    @Test(expected = IncorrectPasswordException.class)
    public void updateEmailFailIncorrectPassword() {
        final User user = mock(User.class);
        final NewEmailDTO newEmailDTO = mock(NewEmailDTO.class);

        when(userRepository.findByIdAndEmailIgnoreCase(anyLong(), anyString())).thenReturn(Optional.of(user));

        String correctPassword = "password";
        when(user.getPassword()).thenReturn(correctPassword);

        String incorrectPassword = "password123";
        when(newEmailDTO.getPassword()).thenReturn(incorrectPassword);

        when(bCryptPasswordEncoder.matches(incorrectPassword, correctPassword)).thenReturn(false);

        userService.updateEmail(1, "test", newEmailDTO);

        fail("Letting a user update their email with an incorrect provided password.");

    }

    @Test(expected = InvalidUpdateException.class)
    public void updateEmailFailSameEmail() {
        final User user = mock(User.class);
        final NewEmailDTO newEmailDTO = mock(NewEmailDTO.class);

        when(userRepository.findByIdAndEmailIgnoreCase(anyLong(), anyString())).thenReturn(Optional.of(user));

        String correctPassword = "password";
        when(user.getPassword()).thenReturn(correctPassword);
        when(newEmailDTO.getPassword()).thenReturn(correctPassword);

        when(bCryptPasswordEncoder.matches(correctPassword, correctPassword)).thenReturn(true);

        when(user.getEmail()).thenReturn("test@email.com");
        when(newEmailDTO.getNewEmail()).thenReturn("test@email.com");

        userService.updateEmail(1, "test", newEmailDTO);

        fail("Letting a user update to an email they already have.");

    }

    @Test(expected = UserAlreadyExistsException.class)
    public void updateEmailFailEmailAlreadyTaken() {
        final User user = mock(User.class);
        final NewEmailDTO newEmailDTO = mock(NewEmailDTO.class);

        when(userRepository.findByIdAndEmailIgnoreCase(anyLong(), anyString())).thenReturn(Optional.of(user));

        String correctPassword = "password";
        when(user.getPassword()).thenReturn(correctPassword);
        when(newEmailDTO.getPassword()).thenReturn(correctPassword);

        when(bCryptPasswordEncoder.matches(correctPassword, correctPassword)).thenReturn(true);

        when(user.getEmail()).thenReturn("test@email.com");
        when(newEmailDTO.getNewEmail()).thenReturn("test2@email.com");

        final User anotherUser = mock(User.class);
        when(userRepository.findByEmailIgnoreCase(newEmailDTO.getNewEmail())).thenReturn(Optional.of(anotherUser));

        userService.updateEmail(1, "test", newEmailDTO);

        fail("Letting a user update to an email taken by another user.");
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updatePasswordFailUserNotFound() {
        final NewPasswordDTO newPasswordDTO = mock(NewPasswordDTO.class);

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        userService.updatePassword(1, newPasswordDTO);

        fail("Exception should have been thrown.");
    }

    @Test(expected = IncorrectPasswordException.class)
    public void updatePasswordFailIncorrectPassword() {
        final User user = mock(User.class);
        final NewPasswordDTO newPasswordDTO = mock(NewPasswordDTO.class);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        String correctPassword = "password";
        when(user.getPassword()).thenReturn(correctPassword);

        String incorrectPassword = "password123";
        when(newPasswordDTO.getOldPassword()).thenReturn(incorrectPassword);

        when(bCryptPasswordEncoder.matches(incorrectPassword, correctPassword)).thenReturn(false);

        userService.updatePassword(1, newPasswordDTO);

        fail("Letting a user update their password even though they provided an incorrect original password.");
    }

}
