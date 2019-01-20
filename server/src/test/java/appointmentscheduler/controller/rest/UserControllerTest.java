package appointmentscheduler.controller.rest;

import appointmentscheduler.AppointmentScheduler;
import appointmentscheduler.dto.user.UserLoginDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.service.AuthenticationService;
import appointmentscheduler.service.appointment.AppointmentService;
import appointmentscheduler.service.user.UserService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
//@WebMvcTest(UserController.class)
//@WebAppConfiguration
@SpringBootTest(classes = {AppointmentScheduler.class})
public class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private AuthenticationService authenticationService;


    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    //    @Test
    public void register() {
    }

    @Test
    public void loginSucceeded() throws Exception {
        final UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setEmail("testEmail");
        userLoginDTO.setPassword("testPassword");

        final Map<String, Object> someMap = Collections.emptyMap();

        when(userService.login(any(UserLoginDTO.class))).thenReturn(someMap);

        final MvcResult result = mockMvc.perform(
                post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginDTO)))
                .andReturn();

        Assert.assertEquals(objectMapper.writeValueAsString(someMap), result.getResponse().getContentAsString());
    }

    @Test
    public void loginFailed() throws Exception {
        final UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setEmail("testEmail");
        userLoginDTO.setPassword("testPassword");

        when(userService.login(any(UserLoginDTO.class))).thenThrow(BadCredentialsException.class);

        final MvcResult result = mockMvc.perform(
                post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginDTO)))
                .andExpect(status().isForbidden())
                .andReturn();

        Assert.assertTrue(result.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void getAppointmentsForCurrentUser() throws Exception {
        Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        User mockUser = new User("firstName", "lastName", "email@gmail.com", "password");
        User mockEmployee = new User("employee", "employeeLastName", "employee@gmail.com", "password2");
        Service service = new Service("serviceName", 20);
        LocalDate date = LocalDate.now();
        LocalTime startTime = LocalTime.now();
        System.out.println(startTime);
        long mockId = 5;
        String note = "note";
        Appointment mockAppointment = new Appointment(mockUser, mockEmployee, service, date, startTime, note);
        List<Appointment> list = new ArrayList<>();
        list.add(mockAppointment);
        Mockito.when(authenticationService.getCurrentUserId()).thenReturn(mockId);
        System.out.println(authenticationService.getCurrentUserId());
        Mockito.when(appointmentService.findByClientId(Mockito.anyLong())).thenReturn(list);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/user/current/appointments").accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println((result.getResponse().getContentAsString()));

    }


}