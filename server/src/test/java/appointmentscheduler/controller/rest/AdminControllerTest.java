
package appointmentscheduler.controller.rest;

import appointmentscheduler.AppointmentScheduler;

import appointmentscheduler.annotation.LogREST;
import appointmentscheduler.entity.employee_service.EmployeeService;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.entity.business.Business;

import appointmentscheduler.repository.EmployeeServiceRepository;
import appointmentscheduler.repository.ServiceRepository;
import appointmentscheduler.service.employee.EmployeeShiftService;
import appointmentscheduler.service.service.ServiceService;
import appointmentscheduler.service.business.BusinessService;
import appointmentscheduler.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
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
import static org.mockito.ArgumentMatchers.anyString;

import static org.mockito.Mockito.*;
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

    @WithMockUser(username = "admin", authorities = "ADMIN" )
    public void assignServiceTest() throws Exception {
        long businessId = 1;
        long employeeId = 1;
        long serviceId = 1;

        Employee mockEmployee = mock(Employee.class);
        Service mockService = mock(Service.class);
        Business mockBusiness =mock(Business.class);
        EmployeeService mockEmployeeService = mock(EmployeeService.class);
        EmployeeShiftService mockEmployeeShiftService = mock( EmployeeShiftService.class);
        ServiceService serviceService = mock(ServiceService.class);
        BusinessService businessService = mock(BusinessService.class);
        EmployeeServiceRepository mockEmployeeServiceRepository = mock(EmployeeServiceRepository.class);


        when(mockBusiness.getId()).thenReturn(businessId);
        when(mockService.getId()).thenReturn(serviceId);
        when(mockEmployee.getId()).thenReturn(employeeId);

        when(mockEmployeeShiftService.getEmployeeByBusinessId(anyLong(), anyLong())).thenReturn(mockEmployee);
        when(serviceService.findByIdAndBusinessId(anyLong(),anyLong())).thenReturn(mockService);
        when(businessService.findById(anyLong())).thenReturn(mockBusiness);
        when(mockEmployeeServiceRepository.save(any(EmployeeService.class))).thenReturn(mockEmployeeService);

        MvcResult result = mockMvc.perform(
                post("/api/business/1/admin/service/991/901")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().isEmpty());

    }


}
