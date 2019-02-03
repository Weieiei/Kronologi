package appointmentscheduler.controller.rest;

import appointmentscheduler.AppointmentScheduler;
import appointmentscheduler.converters.review.ReviewDTOToReview;
import appointmentscheduler.dto.review.ReviewDTO;
import appointmentscheduler.dto.settings.UpdateSettingsDTO;
import appointmentscheduler.dto.user.UpdateEmailDTO;
import appointmentscheduler.dto.user.UpdatePasswordDTO;
import appointmentscheduler.dto.user.UserLoginDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.review.Review;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.exception.IncorrectPasswordException;
import appointmentscheduler.exception.InvalidUpdateException;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.exception.UserAlreadyExistsException;
import appointmentscheduler.service.review.ReviewService;
import appointmentscheduler.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
//@WebMvcTest(UserController.class)
//@WebAppConfiguration
@TestPropertySource(value = "classpath:secret.properties")
@SpringBootTest(classes = {AppointmentScheduler.class})
public class ReviewControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private ReviewDTOToReview reviewConverter;


    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    //    @Test
    public void register() {
    }

    @Test
    public void addReviewSucceeded() throws Exception {
//        final long clientId = 10;
//        final long appointmentId = 10;
//        String content = "content";
//        ReviewDTO reviewDTO = Mockito.mock(ReviewDTO.class);
//        User clientMock = Mockito.mock(User.class);
//        ReviewDTOToReview reviewConverter = Mockito.mock(ReviewDTOToReview.class);
//        Review review = new Review();
//
//
//        when(reviewDTO.getAppointmentId().get()).thenReturn(appointmentId);
//        when(reviewDTO.getClientId()).thenReturn(clientId);
//        when(reviewDTO.getContent()).thenReturn(content);
//
//        when(reviewConverter.convert(reviewDTO)).thenReturn(review);
//
//
//
//
//
//        final MvcResult result = mockMvc.perform(
//                post("/api/reviews")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(reviewDTO)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        assertEquals(objectMapper.writeValueAsString(reviewDTO), result.getResponse().getContentAsString());
    }
}
