package appointmentscheduler.service.review;
import appointmentscheduler.dto.employee.EmployeeShiftDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.review.Review;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.exception.AppEventTimeConflict;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.repository.BusinessRepository;
import appointmentscheduler.repository.EmployeeRepository;
import appointmentscheduler.repository.ReviewRepository;
import appointmentscheduler.repository.ShiftRepository;
import appointmentscheduler.service.employee.EmployeeShiftService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;


    @Mock
   private Review review;

    private ReviewService reviewService;


    @Before
    public void before() {
        reviewService = new ReviewService(reviewRepository);
    }

    @Test
    public void addTest(){
       // Review review = mock(Review.class);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        Map<String, String>  reviewMessage = reviewService.add(review) ;
        assertEquals(reviewMessage,message ("Successfully added review"));
        verify(reviewRepository).save(any(Review.class));
    }

    private Map<String, String> message(String message) {
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        return map;
    }

    @Test
    public void findByAppointmentIdTest(){
        long id = 1;
        Appointment appointment = mock(Appointment.class);
        when(appointment.getId()).thenReturn(id);
        when(reviewRepository.findByAppointmentId(anyLong())).thenReturn(Optional.of(review));
        reviewService.findByAppointmentId(appointment.getId());
        verify(reviewRepository).findByAppointmentId(anyLong());

    }
}
