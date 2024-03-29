package appointmentscheduler.service.appointment;

import appointmentscheduler.dto.appointment.AppointmentDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.employee_service.EmployeeService;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.exception.*;
import appointmentscheduler.repository.*;
import appointmentscheduler.service.email.EmailService;
import appointmentscheduler.service.googleService.GoogleSyncService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

//TODO fix ignored tests + stubbing
@RunWith(MockitoJUnitRunner.Silent.class)
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ShiftRepository shiftRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private BusinessRepository businessRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private GeneralAppointmentRepository generalAppointmentRepository;

    @Mock
    private GoogleCredentialRepository googleCredentialRepository;

    @Mock
    private GoogleSyncService googleSyncService;


    @Mock
    private CancelledRepository cancelledRepository;

    @Mock
    private GuestRepository guestRepository;

    private AppointmentService appointmentService;


    @Before
    public void setup() {
        appointmentService = new AppointmentService(appointmentRepository, employeeRepository, shiftRepository, cancelledRepository, userRepository, serviceRepository, businessRepository, emailService, generalAppointmentRepository, googleCredentialRepository, googleSyncService, guestRepository);
    }

    public void mockGetAppointment(User user, Employee employee, Service service, Business business) {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(employeeRepository.findByIdAndBusinessId(anyLong(), anyLong())).thenReturn(Optional.of(employee));
        when(serviceRepository.findByIdAndBusinessId(anyLong(), anyLong())).thenReturn(Optional.of(service));
        when(businessRepository.findById(anyLong())).thenReturn(Optional.of(business));
    }

    @Test(expected = ModelValidationException.class)
    public void addShouldFailWhenClientAndEmployeeAreSame() {
        final int ID = 1;

        // Must create real objects because Mockito can't mock equals method
        final User client = new User();
        client.setId(ID);

        // Must create real objects because Mockito can't mock equals method
        final Employee employee = new Employee();
        employee.setId(ID);

        AppointmentDTO mockAppointmentDTO = new AppointmentDTO();
        final Service mockService = mock(Service.class);
        final Business mockBusiness = mock(Business.class);

        mockAppointmentDTO.setDate(LocalDate.now());
        mockAppointmentDTO.setStartTime(LocalTime.now());

        when(mockService.getDuration()).thenReturn(1);
        mockGetAppointment(client, employee, mockService, mockBusiness);

        appointmentService.add(mockAppointmentDTO, 1, 1);
    }




    @Test(expected = ClientAppointmentConflictException.class)
    public void addShouldFailBecauseClientIsBookedAlready() {
        AppointmentDTO mockAppointmentDTO = new AppointmentDTO();
        final Employee mockEmployee = mock(Employee.class);
        final Service mockService = mock(Service.class);
        final Shift mockShift = mock(Shift.class);
        final Business mockBusiness = mock(Business.class);
        EmployeeService mockEmployeeService = mock(EmployeeService.class);
        final User client = mock(User.class);
        final long empId = 1;
        HashSet<EmployeeService> employeeServiceHashSet = new HashSet<>();
        List<Appointment> clientAppointments = new ArrayList<>();

        //set DTO times, will be converted into appointment object in service
        mockAppointmentDTO.setDate(LocalDate.now());
        mockAppointmentDTO.setStartTime(LocalTime.now());
        when(mockService.getDuration()).thenReturn(1);

        when(mockEmployee.getId()).thenReturn(empId);

        //add employee to selected service
        when(mockEmployeeService.getEmployee()).thenReturn(mockEmployee);
        employeeServiceHashSet.add(mockEmployeeService);
        when(mockService.getEmployees()).thenReturn(employeeServiceHashSet);

        //employee is available during appointment time
        when(mockEmployee.isAvailable(any())).thenReturn(mockShift);

        mockGetAppointment(client, mockEmployee, mockService, mockBusiness);

        //Set employee to have no appointments
        when(appointmentRepository.findByDateAndEmployeeIdAndBusinessIdAndStatus(any(), anyLong(), anyLong(), any())).thenReturn(Collections.emptyList());

        //Add appointment for client that conflicts with new appointment
        clientAppointments.add(new Appointment(client, mockEmployee, mockService, LocalDate.now(),LocalTime.now(), "mock", mockBusiness));
        when(appointmentRepository.findByDateAndClientIdAndBusinessIdAndStatus(any(), anyLong(), anyLong(), any())).thenReturn(clientAppointments);

        appointmentService.add(mockAppointmentDTO, 1, 1);
    }

    @Test
    public void addPassesWithNoConflictingAppointments() {
        AppointmentDTO mockAppointmentDTO = new AppointmentDTO();
        final Employee mockEmployee = mock(Employee.class);
        final Service mockService = mock(Service.class);
        final Business mockBusiness = mock(Business.class);
        final Shift mockShift = mock(Shift.class);
        EmployeeService mockEmployeeService = mock(EmployeeService.class);
        final User client = mock(User.class);
        final long empId = 1;

        HashSet<EmployeeService> employeeServiceHashSet = new HashSet<>();

        //set DTO times, will be converted into appointment object in service
        mockAppointmentDTO.setDate(LocalDate.now());
        mockAppointmentDTO.setStartTime(LocalTime.now());
        when(mockService.getDuration()).thenReturn(1);

        when(mockEmployee.getId()).thenReturn(empId);

        //add employee to selected service
        when(mockEmployeeService.getEmployee()).thenReturn(mockEmployee);
        employeeServiceHashSet.add(mockEmployeeService);
        when(mockService.getEmployees()).thenReturn(employeeServiceHashSet);

        //employee is available during appointment time
        when(mockEmployee.isAvailable(any())).thenReturn(mockShift);

        mockGetAppointment(client, mockEmployee, mockService, mockBusiness);

        //Set employee to have no appointments
        when(appointmentRepository.findByDateAndEmployeeIdAndBusinessIdAndStatus(any(), anyLong(), anyLong(), any())).thenReturn(Collections.emptyList());

        //Add appointment for client that conflicts with new appointment
        when(appointmentRepository.findByDateAndClientIdAndBusinessIdAndStatus(any(), anyLong(), anyLong(), any())).thenReturn(Collections.emptyList());

        appointmentService.add(mockAppointmentDTO, 1, 1);

        verify(appointmentRepository, times(1)).save(any());

    }

    @Test
    public void addSeveralPassesWithNoConflictingAppointments() {
        List<AppointmentDTO> mockAppointmentDTOS = new ArrayList<>();
        final Employee mockEmployee = mock(Employee.class);
        final Service mockService = mock(Service.class);
        final Business mockBusiness = mock(Business.class);
        final Shift mockShift = mock(Shift.class);
        EmployeeService mockEmployeeService = mock(EmployeeService.class);
        final User client = mock(User.class);
        final long empId = 1;
        final int numApp = 5;
        LocalDate localDate = LocalDate.now();

        HashSet<EmployeeService> employeeServiceHashSet = new HashSet<>();

        //set DTO times, will be converted into appointment object in service
        for(int i = 0; i < numApp;i++){
            //put appointments on different days
            mockAppointmentDTOS.add(new AppointmentDTO());
            mockAppointmentDTOS.get(i).setDate(localDate.plusDays(i));
            mockAppointmentDTOS.get(i).setStartTime(LocalTime.now());
        }
        when(mockService.getDuration()).thenReturn(1);

        when(mockEmployee.getId()).thenReturn(empId);

        //add employee to selected service
        when(mockEmployeeService.getEmployee()).thenReturn(mockEmployee);
        employeeServiceHashSet.add(mockEmployeeService);
        when(mockService.getEmployees()).thenReturn(employeeServiceHashSet);

        //employee is available during appointment time
        when(mockEmployee.isAvailable(any())).thenReturn(mockShift);

        mockGetAppointment(client, mockEmployee, mockService, mockBusiness);

        //Set employee to have no appointments
        when(appointmentRepository.findByDateAndEmployeeIdAndBusinessIdAndStatus(any(), anyLong(), anyLong(), any())).thenReturn(Collections.emptyList());

        //Set client to have no appointments
        when(appointmentRepository.findByDateAndClientIdAndBusinessIdAndStatus(any(), anyLong(), anyLong(), any())).thenReturn(Collections.emptyList());

        appointmentService.addList(mockAppointmentDTOS, 1, 1);

        verify(appointmentRepository, times(numApp)).save(any());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateAppointmentFailNotFound() {
        final AppointmentDTO mockAppointment = mock(AppointmentDTO.class, RETURNS_DEEP_STUBS);

        final Service mockService = mock(Service.class);
        final Business mockBusiness = mock(Business.class);
        final Employee employee = mock(Employee.class);
        final User client = mock(User.class);


        mockGetAppointment(client, employee, mockService, mockBusiness);

        when(appointmentRepository.findByIdAndBusinessIdAndClientId(anyLong(), anyLong(), anyLong())).thenReturn(Optional.empty());
        appointmentService.update(mockAppointment, 1, 1, 1);
    }

    @Test
    public void updateAppointmentSuccess() {
        AppointmentDTO mockAppointmentDTO = new AppointmentDTO();
        Appointment retrievedApp = new Appointment();
        final Employee mockEmployee = mock(Employee.class);
        final Service mockService = mock(Service.class);
        final Business mockBusiness = mock(Business.class);
        final Shift mockShift = mock(Shift.class);
        EmployeeService mockEmployeeService = mock(EmployeeService.class);
        final User client = mock(User.class);
        final long empId = 1;

        HashSet<EmployeeService> employeeServiceHashSet = new HashSet<>();

        //set DTO times, will be converted into appointment object in service
        mockAppointmentDTO.setDate(LocalDate.now());
        mockAppointmentDTO.setStartTime(LocalTime.now());
        when(mockService.getDuration()).thenReturn(1);

        when(mockEmployee.getId()).thenReturn(empId);

        //add employee to selected service
        when(mockEmployeeService.getEmployee()).thenReturn(mockEmployee);
        employeeServiceHashSet.add(mockEmployeeService);
        when(mockService.getEmployees()).thenReturn(employeeServiceHashSet);

        //employee is available during appointment time
        when(mockEmployee.isAvailable(any())).thenReturn(mockShift);

        mockGetAppointment(client, mockEmployee, mockService, mockBusiness);

        //Set employee to have no appointments
        when(appointmentRepository.findByDateAndEmployeeIdAndBusinessIdAndStatus(any(), anyLong(), anyLong(), any())).thenReturn(Collections.emptyList());

        //Set client to have no appointments
        when(appointmentRepository.findByDateAndClientIdAndBusinessIdAndStatus(any(), anyLong(), anyLong(), any())).thenReturn(Collections.emptyList());

        //Get converted appointment from DTO
        retrievedApp = mockAppointmentDTO.convertToAppointment(client, mockEmployee, mockService, mockBusiness);
        when(appointmentRepository.findByIdAndBusinessIdAndClientId(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(retrievedApp));
        when(appointmentRepository.save(any())).thenReturn(retrievedApp);

        appointmentService.update(mockAppointmentDTO, 1, 1, 1);

        verify(appointmentRepository, times(1)).save(any());
    }

}
