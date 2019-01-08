package appointmentscheduler.service.user;

import appointmentscheduler.dto.user.UserLoginDTO;
import appointmentscheduler.dto.user.UserRegisterDTO;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.exception.UserAlreadyExistsException;
import appointmentscheduler.repository.RoleRepository;
import appointmentscheduler.repository.UserRepository;
import appointmentscheduler.util.JwtProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    private UserService userService;

    @Before
    public void before() {
        userService = new UserService(userRepository, roleRepository, jwtProvider, bCryptPasswordEncoder, authenticationManager);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void registerFailed() {
        // create mocks
        final User mockedUser = mock(User.class);
        final UserRegisterDTO userRegisterDTO = mock(UserRegisterDTO.class);

        // mock methods
        when(userRegisterDTO.getEmail()).thenReturn("testEmail");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockedUser));

        // run method
        userService.register(userRegisterDTO);

        // fail if it didn't throw an error
        fail("Exception should have been thrown");
    }

    @Test
    public void registerSucceeded() {
        fail("todo");
    }

    @Test(expected = BadCredentialsException.class)
    public void loginFailed() {
        // create mocks
        final UserLoginDTO userLoginDTO = mock(UserLoginDTO.class);

        // mock methods
        when(userLoginDTO.getEmail()).thenReturn("testEmail");
        when(userLoginDTO.getPassword()).thenReturn("testPassword");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

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

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockedUser));
        when(jwtProvider.generateToken(any())).thenReturn("testToken");

        // run method and get result
        Map map = userService.login(userLoginDTO);

        // verify result contents for user
        assertNotNull(map.get("user"));
        assertEquals(1, ((User) map.get("user")).getId());

        // verify result contents for token
        assertNotNull(map.get("token"));
        assertEquals("testToken", map.get("token"));
    }
}