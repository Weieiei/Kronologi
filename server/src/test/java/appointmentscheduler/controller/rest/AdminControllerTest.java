package appointmentscheduler.controller.rest;

import appointmentscheduler.AppointmentScheduler;
import appointmentscheduler.entity.role.Role;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.service.user.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
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

    @Test
    public void changeRoleToEmployee() throws Exception {
        User mockUser = mock(User.class);
        Set<Role> roles = new HashSet<>();
        Role empRole = new Role(RoleEnum.EMPLOYEE);
        roles.add(empRole);
        when(mockUser.getRoles()).thenReturn(roles);
        when(userService.updateUser(any(User.class))).thenReturn(true);
        MvcResult result = mockMvc.perform(
                post("/api/admin/change_to_employee")
                        .contentType(MediaType.APPLICATION_JSON).content("1"))
                .andReturn();
        Assert.assertEquals("Successfully changed user to employee.", result.getResponse().getContentAsString());

        when(userService.updateUser(any(User.class))).thenReturn(false);
        result = mockMvc.perform(
                post("/api/admin/change_to_employee")
                        .contentType(MediaType.APPLICATION_JSON).content("1"))
                .andReturn();
        Assert.assertEquals("Something went wrong while updating user, please check the server log.", result.getResponse().getContentAsString());
    }
}