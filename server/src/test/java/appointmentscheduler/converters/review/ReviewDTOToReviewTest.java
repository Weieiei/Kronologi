package appointmentscheduler.converters.review;

import appointmentscheduler.dto.review.ReviewDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.review.Review;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.exception.ReviewAlreadyExistsException;
import appointmentscheduler.exception.UserNotAllowedToReviewServiceException;
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

import java.io.Console;
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
    Appointment appointmentMock;

    @Mock
    Review reviewMock;

    @Mock
    User clientMock;

    private ReviewDTOToReview converter;

    @Before
    public void setup(){

            converter = new ReviewDTOToReview(userRepositoryMock,appointmentRepositoryMock, reviewRepositoryMock);

    }

    @Test
    public void convertReviewDTOToReviewSuccessTest(){

        long appointmentId = 5;
        long clientId = 3;
        String content = "my content";

        when(reviewMock.getAppointment()).thenReturn(appointmentMock);
        when(reviewMock.getClient()).thenReturn(clientMock);
        when(reviewMock.getContent()).thenReturn(content);
        //review and appointment have matching id
        when(reviewDTO.getContent()).thenReturn(content);
        when(reviewDTO.getAppointmentId()).thenReturn(appointmentId);
        when(reviewDTO.getClientId()).thenReturn(clientId);
        when(appointmentMock.getId()).thenReturn(appointmentId);
        when(appointmentMock.getClient()).thenReturn(clientMock);
        when(appointmentMock.getClient().getId()).thenReturn(clientId);
        when( appointmentRepositoryMock.findById(anyLong()) ).thenReturn(Optional.of(appointmentMock) );
        when(userRepositoryMock.findById(anyLong())).thenReturn(Optional.of(clientMock));
        if (appointmentRepositoryMock.findById(anyLong()).isPresent()){
            Review actualResult = converter.convert(reviewDTO);
            Assert.assertEquals(reviewMock.getAppointment(), actualResult.getAppointment());
            Assert.assertEquals(reviewMock.getClient(), actualResult.getClient());
            Assert.assertEquals(reviewMock.getContent(), actualResult.getContent());
        }
        else
            fail();
    }

    @Test
    public void convertWithAppointmentNotAssignedToUserTest()
    {

        long appointmentId = 5;
        long clientId = 3;
        //review and appointment have matching id
        Mockito.when(reviewDTO.getAppointmentId()).thenReturn((long) 100);
        Mockito.when(reviewDTO.getClientId()).thenReturn(clientId);
        Mockito.when(appointmentMock.getId()).thenReturn(appointmentId);
        Mockito.when(appointmentMock.getClient()).thenReturn(clientMock);
        Mockito.when(appointmentMock.getClient().getId()).thenReturn(clientId);
        Mockito.when( appointmentRepositoryMock.findById(anyLong()) ).thenReturn(Optional.of(appointmentMock) );
        //give a review for the appointment
        Mockito.when( reviewRepositoryMock.findByAppointmentId(anyLong()) ).thenReturn(Optional.of(reviewMock) );
        try {
            converter.convert(reviewDTO);
            fail();
        }
        catch (UserNotAllowedToReviewServiceException e){
            Assert.assertEquals(String.format("User with id %d is not authorized to" +
                            " write a review for appointment with id %d. This appointment was not assigned to " +
                            "them.",
                    reviewDTO.getClientId(), reviewDTO.getAppointmentId()),
                    e.getMessage());
        }
    }

    @Test
    public void convertWithAppointmentAlreadyHasReviewTest()
    {

        long appointmentId = 5;
        long clientId = 3;
        //review and appointment have matching id
        Mockito.when(reviewDTO.getAppointmentId()).thenReturn(appointmentId);
        Mockito.when(reviewDTO.getClientId()).thenReturn(clientId);
        Mockito.when(appointmentMock.getId()).thenReturn(appointmentId);
        Mockito.when(appointmentMock.getClient()).thenReturn(clientMock);
        Mockito.when(appointmentMock.getClient().getId()).thenReturn(clientId);
        Mockito.when( appointmentRepositoryMock.findById(anyLong()) ).thenReturn(Optional.of(appointmentMock) );
        //give a review for the appointment
        Mockito.when( reviewRepositoryMock.findByAppointmentId(anyLong()) ).thenReturn(Optional.of(reviewMock) );
        try {
            converter.convert(reviewDTO);
            fail();
        }
        catch (ReviewAlreadyExistsException e){
            Assert.assertEquals(String.format("Appointment with id %d already has a review.",appointmentId),
                    e.getMessage());
        }
    }

    @Test
    public void convertWithInvalidAppointmentIdTest()
    {

        long appointmentId = 5;
        Mockito.when(reviewDTO.getAppointmentId()).thenReturn(appointmentId);
        Mockito.when( appointmentRepositoryMock.findById(appointmentId) ).thenReturn(Optional.empty());
        try {
            converter.convert(reviewDTO);
            fail();
        }
        catch (ResourceNotFoundException e){
            Assert.assertEquals((String.format("Appointment with id %d not found.",
                    reviewDTO.getAppointmentId())),
                    e.getMessage());
        }
    }

}
