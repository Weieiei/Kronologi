package appointmentscheduler.controller.rest;

import appointmentscheduler.AppointmentScheduler;
import appointmentscheduler.dto.user.UserLoginDTO;
import appointmentscheduler.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
//@WebMvcTest(UserController.class)
//@WebAppConfiguration
@TestPropertySource(value = "classpath:secret.properties")
@SpringBootTest(classes = {AppointmentScheduler.class})
public class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

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
}