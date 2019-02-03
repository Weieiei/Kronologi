package appointmentscheduler.controller.rest;

import appointmentscheduler.AppointmentScheduler;
import appointmentscheduler.dto.service.ServiceDTO;
import appointmentscheduler.dto.user.UserLoginDTO;
import appointmentscheduler.entity.role.Role;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.user.User;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@TestPropertySource(value = "classpath:secret.properties")
@SpringBootTest(classes = {AppointmentScheduler.class})
public class AdminControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void changeRoleToEmployee() throws Exception {
        User mockUser = mock(User.class);
        Set<Role> roles = new HashSet<>();
        Role employeeRole = new Role(RoleEnum.EMPLOYEE);
        roles.add(employeeRole);
        when(mockUser.getRoles()).thenReturn(roles);
        when(userService.updateUser(any(User.class))).thenReturn(true);

        final ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setId(1);

        MvcResult result = mockMvc.perform(
                post("/api/admin/change_to_employee/")
                        .with(request -> {
                            request.setAttribute("userId", 2);
                            request.setAttribute("id", 1);
                            request.setAttribute("name", "toto");
                            request.setAttribute("duration", 99999);
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(serviceDTO)))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().isEmpty());

        when(userService.updateUser(any(User.class))).thenReturn(false);
        result = mockMvc.perform(
                post("/api/admin/change_to_employee")
                        .contentType(MediaType.APPLICATION_JSON).content("1"))
                .andExpect(status().isOk())
                .andReturn();
        Assert.assertEquals("Something went wrong while updating user, please check the server log.", result.getResponse().getContentAsString());
    }

    @Test
    public void assignService() throws Exception {
    }
}
