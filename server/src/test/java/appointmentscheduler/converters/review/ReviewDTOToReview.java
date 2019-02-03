package appointmentscheduler.converters.review;

import appointmentscheduler.dto.review.ReviewDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.repository.AppointmentRepository;
import appointmentscheduler.repository.ReviewRepository;
import appointmentscheduler.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

public class ReviewDTOToReview {

    @InjectMocks
    UserRepository userRepositoryMock;

    @InjectMocks
    AppointmentRepository appointmentRepositoryMock;

    @InjectMocks
    ReviewRepository reviewRepositoryMock;

    @Mock
    ReviewDTO reviewDTO;


    @Before
    public void setup(){

            ReviewDTOToReview converter = new ReviewDTOToReview();
    }

    @Test
    public void convertTest()
    {
//        when(reviewDTO.getAppointmentId()).getMock().
        when(appointmentRepositoryMock.findById(reviewDTO.getAppointmentId()).isPresent()).thenReturn(true);
    }

}
