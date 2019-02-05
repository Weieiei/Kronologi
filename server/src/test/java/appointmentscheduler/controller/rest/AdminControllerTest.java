package appointmentscheduler.controller.rest;

import appointmentscheduler.AppointmentScheduler;
import appointmentscheduler.entity.role.Role;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.service.ServiceEntity;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.repository.RoleRepository;
import appointmentscheduler.repository.ServiceRepository;
import appointmentscheduler.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.*;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
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
    public void changeRoleToEmployeeTest() throws Exception {
        User mockUser = mock(User.class);
        Set<Role> roles = new HashSet<>();
        // to test the conflict in case the user is already an employee, we add employee to the role set:
        Role employeeRole = new Role(RoleEnum.EMPLOYEE);
        roles.add(employeeRole);
        when(mockUser.getRoles()).thenReturn(roles);
        when(userService.updateUser(any(User.class))).thenReturn(true);
        when(userService.findUserByid(anyLong())).thenReturn(mockUser);

        MvcResult result = mockMvc.perform(
                post("/api/admin/user/employee")
                        .with(request -> {
                            request.setAttribute("id", 1);
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().isEmpty());

        when(userService.updateUser(any(User.class))).thenReturn(false);
        result = mockMvc.perform(
                post("/api/admin/user/employee")
                        .contentType(MediaType.APPLICATION_JSON).content("1"))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().isEmpty());



    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void assignServiceTest() throws Exception {
        User mockEmployee = mock(User.class);
        ServiceEntity mockService = mock(ServiceEntity.class);
        Optional<ServiceEntity> service = Optional.of(mockService);
        RoleRepository mockRoleRepository = mock(RoleRepository.class);
        ServiceRepository mockServiceRepository = mock(ServiceRepository.class);
        Set<Role> roles = new HashSet<>();
        Role employeeRole = new Role(RoleEnum.EMPLOYEE);
        roles.add(employeeRole);
        when(mockEmployee.getRoles()).thenReturn(roles);
        when(userService.updateUser(any(User.class))).thenReturn(true);
        when(userService.findUserByid(anyLong())).thenReturn(mockEmployee);
        when(mockRoleRepository.findByRole(any(RoleEnum.class))).thenReturn(new Role(RoleEnum.EMPLOYEE));
        when(mockServiceRepository.findById(anyLong())).thenReturn(service);
        List<ServiceEntity> serviceList = new ArrayList<>();
        serviceList.add(mockService);
        when(mockEmployee.getEmployeeServices()).thenReturn(serviceList);

        MvcResult result = mockMvc.perform(
                post("/api/admin/service/1/1")
                        .with(request -> {
                            request.setAttribute("employeeId", 1);
                            request.setAttribute("serviceId", 1);
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().isEmpty());

        when(userService.updateUser(any(User.class))).thenReturn(false);
        when(mockRoleRepository.findByRole(any(RoleEnum.class))).thenReturn(null);
        result = mockMvc.perform(
                post("/api/admin/service/991/901")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().isEmpty());

    }


}
