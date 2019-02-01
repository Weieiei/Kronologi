package appointmentscheduler.service.verification;

import appointmentscheduler.entity.user.User;
import appointmentscheduler.entity.verification.Verification;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.repository.UserRepository;
import appointmentscheduler.repository.VerificationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VerificationServiceTest {

    @Mock
    private VerificationRepository verificationRepository;

    @Mock
    private UserRepository userRepository;

    private VerificationService verificationService;

    @Before
    public void before() {
        verificationService = new VerificationService(userRepository, verificationRepository);
    }

    @Test
    public void successHashVerification() {
        Verification verification = mock(Verification.class);
        final User mockedUser = mock(User.class);

        when(verification.getUser()).thenReturn(mockedUser);
        when(verificationRepository.findByHash(anyString())).thenReturn(verification);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockedUser));
        when(mockedUser.getId()).thenReturn(anyLong());

        assertTrue(verificationService.verify("hash"));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void invalidHashVerify() {
        Verification verification = null;
        when(verificationRepository.findByHash(anyString())).thenReturn(verification);
        verificationService.verify("test");
        // fail if it didn't throw an error
        fail("Exception should have been thrown");
    }
}