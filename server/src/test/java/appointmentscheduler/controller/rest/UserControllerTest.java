package appointmentscheduler.controller.rest;

import appointmentscheduler.AppointmentScheduler;
import appointmentscheduler.dto.settings.UpdateSettingsDTO;
import appointmentscheduler.dto.user.UpdateEmailDTO;
import appointmentscheduler.dto.user.UpdatePasswordDTO;
import appointmentscheduler.dto.user.UserLoginDTO;
import appointmentscheduler.exception.IncorrectPasswordException;
import appointmentscheduler.exception.InvalidUpdateException;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.exception.UserAlreadyExistsException;
import appointmentscheduler.service.user.UserService;
import appointmentscheduler.service.verification.VerificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @MockBean
    private VerificationService verificationService;

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
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(objectMapper.writeValueAsString(someMap), result.getResponse().getContentAsString());
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

        assertTrue(result.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void verificationSucceeded() throws Exception {
        String testHash = "testHash";
        when(verificationService.verify(testHash)).thenReturn(true);
        final MvcResult result = mockMvc.perform(
                get("/api/user/verification?hash=testHash")
        )
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    public void verificationFailed() throws Exception {
        final MvcResult result = mockMvc.perform(
                get("/api/user/verification")
        )
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void updateEmailFailUserNotFound() throws Exception {
        final UpdateEmailDTO updateEmailDTO = new UpdateEmailDTO();
        updateEmailDTO.setPassword("test123");
        updateEmailDTO.setNewEmail("test@test.com");

        when(userService.updateEmail(anyLong(), anyString(), any(UpdateEmailDTO.class))).thenThrow(ResourceNotFoundException.class);

        final MvcResult result = mockMvc.perform(
                post("/api/user/email")
                        .with(request -> {
                            request.setAttribute("userId", 1);
                            request.setAttribute("email", "test@test.com");
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateEmailDTO))
        )
                .andExpect(status().isNotFound())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void updateEmailFailIncorrectPassword() throws Exception {
        final UpdateEmailDTO updateEmailDTO = new UpdateEmailDTO();
        updateEmailDTO.setPassword("test123");
        updateEmailDTO.setNewEmail("test@test.com");

        when(userService.updateEmail(anyLong(), anyString(), any(UpdateEmailDTO.class))).thenThrow(IncorrectPasswordException.class);

        final MvcResult result = mockMvc.perform(
                post("/api/user/email")
                        .with(request -> {
                            request.setAttribute("userId", 1);
                            request.setAttribute("email", "test@test.com");
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateEmailDTO))
        )
                .andExpect(status().isBadRequest())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void updateEmailFailInvalidUpdate() throws Exception {
        final UpdateEmailDTO updateEmailDTO = new UpdateEmailDTO();
        updateEmailDTO.setPassword("test123");
        updateEmailDTO.setNewEmail("test@test.com");

        when(userService.updateEmail(anyLong(), anyString(), any(UpdateEmailDTO.class))).thenThrow(InvalidUpdateException.class);

        final MvcResult result = mockMvc.perform(
                post("/api/user/email")
                        .with(request -> {
                            request.setAttribute("userId", 1);
                            request.setAttribute("email", "test@test.com");
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateEmailDTO))
        )
                .andExpect(status().isBadRequest())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void updateEmailFailEmailTaken() throws Exception {
        final UpdateEmailDTO updateEmailDTO = new UpdateEmailDTO();
        updateEmailDTO.setPassword("test123");
        updateEmailDTO.setNewEmail("test@test.com");

        when(userService.updateEmail(anyLong(), anyString(), any(UpdateEmailDTO.class))).thenThrow(UserAlreadyExistsException.class);

        final MvcResult result = mockMvc.perform(
                post("/api/user/email")
                        .with(request -> {
                            request.setAttribute("userId", 1);
                            request.setAttribute("email", "test@test.com");
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateEmailDTO))
        )
                .andExpect(status().isBadRequest())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void updateEmailSuccess() throws Exception {
        final UpdateEmailDTO updateEmailDTO = new UpdateEmailDTO();
        updateEmailDTO.setPassword("test123");
        updateEmailDTO.setNewEmail("newtest@test.com");

        final Map<String, String> map = Collections.emptyMap();

        when(userService.updateEmail(anyLong(), anyString(), any(UpdateEmailDTO.class))).thenReturn(map);

        final MvcResult result = mockMvc.perform(
                post("/api/user/email")
                        .with(request -> {
                            request.setAttribute("userId", 1);
                            request.setAttribute("email", "test@test.com");
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateEmailDTO))
        )
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(objectMapper.writeValueAsString(map), result.getResponse().getContentAsString());
    }

    @Test
    public void updatePasswordFailUserNotFound() throws Exception {
        final UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
        updatePasswordDTO.setOldPassword("test");
        updatePasswordDTO.setOldPassword("newtest");

        when(userService.updatePassword(anyLong(), any(UpdatePasswordDTO.class))).thenThrow(ResourceNotFoundException.class);

        final MvcResult result = mockMvc.perform(
                post("/api/user/password")
                        .with(request -> {
                            request.setAttribute("userId", 1);
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePasswordDTO))
        )
                .andExpect(status().isNotFound())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void updatePasswordFailIncorrectPassword() throws Exception {
        final UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
        updatePasswordDTO.setOldPassword("test");
        updatePasswordDTO.setOldPassword("newtest");

        when(userService.updatePassword(anyLong(), any(UpdatePasswordDTO.class))).thenThrow(IncorrectPasswordException.class);

        final MvcResult result = mockMvc.perform(
                post("/api/user/password")
                        .with(request -> {
                            request.setAttribute("userId", 1);
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePasswordDTO))
        )
                .andExpect(status().isBadRequest())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void updatePasswordSuccess() throws Exception {
        final UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
        updatePasswordDTO.setOldPassword("test");
        updatePasswordDTO.setOldPassword("newtest");

        final Map<String, String> map = Collections.emptyMap();

        when(userService.updatePassword(anyLong(), any(UpdatePasswordDTO.class))).thenReturn(map);

        final MvcResult result = mockMvc.perform(
                post("/api/user/password")
                        .with(request -> {
                            request.setAttribute("userId", 1);
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePasswordDTO))
        )
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(objectMapper.writeValueAsString(map), result.getResponse().getContentAsString());
    }

    @Test
    public void updateSettingsFailUserNotFound() throws Exception {
        final UpdateSettingsDTO updateSettingsDTO = new UpdateSettingsDTO();
        updateSettingsDTO.setEmailReminder(true);
        updateSettingsDTO.setTextReminder(true);

        when(userService.updateSettings(anyLong(), any(UpdateSettingsDTO.class))).thenThrow(ResourceNotFoundException.class);

        final MvcResult result = mockMvc.perform(
                post("/api/user/settings")
                        .with(request -> {
                            request.setAttribute("userId", 1);
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateSettingsDTO))
        )
                .andExpect(status().isNotFound())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void updateSettingsSuccess() throws Exception {
        final UpdateSettingsDTO updateSettingsDTO = new UpdateSettingsDTO();
        updateSettingsDTO.setEmailReminder(true);
        updateSettingsDTO.setTextReminder(true);

        final Map<String, String> map = Collections.emptyMap();

        when(userService.updateSettings(anyLong(), any(UpdateSettingsDTO.class))).thenReturn(map);

        final MvcResult result = mockMvc.perform(
                post("/api/user/settings")
                        .with(request -> {
                            request.setAttribute("userId", 1);
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateSettingsDTO))
        )
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(objectMapper.writeValueAsString(map), result.getResponse().getContentAsString());
    }

    @Test
    public void deletePhoneNumberFailUserDoesntHavePhoneNumber() throws Exception {
        when(userService.deletePhoneNumber(anyLong())).thenThrow(ResourceNotFoundException.class);

        final MvcResult result = mockMvc.perform(
                delete("/api/user/phone")
                        .with(request -> {
                            request.setAttribute("userId", 1);
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void deletePhoneNumberSuccess() throws Exception {
        final Map<String, String> map = Collections.emptyMap();

        when(userService.deletePhoneNumber(anyLong())).thenReturn(map);

        final MvcResult result = mockMvc.perform(
                delete("/api/user/phone")
                        .with(request -> {
                            request.setAttribute("userId", 1);
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(objectMapper.writeValueAsString(map), result.getResponse().getContentAsString());
    }
}
