package appointmentscheduler.converters.review;

import appointmentscheduler.dto.review.ReviewDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.review.Review;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.exception.ReviewAlreadyExistsException;
import appointmentscheduler.repository.AppointmentRepository;
import appointmentscheduler.repository.ReviewRepository;
import appointmentscheduler.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
@RunWith(MockitoJUnitRunner.class)
public class ReviewDTOToReviewTest {

    @Mock
    UserRepository userRepositoryMock;

    @Mock
    AppointmentRepository appointmentRepositoryMock;

    @Mock
    ReviewRepository reviewRepositoryMock;

    @Mock
    ReviewDTO reviewDTO;

    @Mock
    Appointment appointment;

    @Mock
    Review review;

    @Mock
    User client;

    private ReviewDTOToReview converter;

    @Before
    public void setup(){

            converter = new ReviewDTOToReview(userRepositoryMock,appointmentRepositoryMock, reviewRepositoryMock);

    }

    @Test
    public void convertWithExistingReviewTest()
    {

        long appointmentId = 5;
        long clientId = 3;
        //review and appointment have matching id
        Mockito.when(reviewDTO.getAppointmentId()).thenReturn(appointmentId);
        Mockito.when(reviewDTO.getClientId()).thenReturn(clientId);
        Mockito.when(appointment.getId()).thenReturn(appointmentId);
        Mockito.when(appointment.getClient()).thenReturn(client);
        Mockito.when(appointment.getClient().getId()).thenReturn(clientId);
        Mockito.when( appointmentRepositoryMock.findById(anyLong()) ).thenReturn(Optional.of(appointment) );
        //give a review for the appointment
        Mockito.when( reviewRepositoryMock.findByAppointmentId(anyLong()) ).thenReturn(Optional.of(review) );
        try {
            converter.convert(reviewDTO);
            fail();
        }
        catch (ReviewAlreadyExistsException e){
            Assert.assertEquals("Appointment with id " +appointmentId+" already has a review.", e.getMessage());
        }
    }

}
