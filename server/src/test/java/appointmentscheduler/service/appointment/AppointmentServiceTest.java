package appointmentscheduler.service.appointment;

import appointmentscheduler.dto.appointment.AppointmentDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.employee_service.EmployeeService;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.entity.verification.GoogleCred;
import appointmentscheduler.exception.*;
import appointmentscheduler.repository.*;
import appointmentscheduler.service.email.EmailService;
import appointmentscheduler.service.googleService.GoogleSyncService;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.Answers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
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
    private AppointmentService appointmentService;

    @Before
    public void setup() {
        appointmentService = new AppointmentService(appointmentRepository, employeeRepository, shiftRepository, cancelledRepository, userRepository, serviceRepository, businessRepository, emailService, generalAppointmentRepository, googleCredentialRepository, googleSyncService);
    }

    public void mockGetAppointment(User user, Employee employee, Service service, Business business) {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(employeeRepository.findByIdAndBusinessId(anyLong(), anyLong())).thenReturn(Optional.of(employee));
        when(serviceRepository.findByIdAndBusinessId(anyLong(), anyLong())).thenReturn(Optional.of(service));
        when(businessRepository.findById(anyLong())).thenReturn(Optional.of(business));
        //when(googleCredentialRepository.findByKey(anyString())).thenReturn(Optional.of())
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

    @Test(expected = EmployeeDoesNotOfferServiceException.class)
    public void addShouldFailBecauseEmployeeDoesNotOfferService() {
        AppointmentDTO mockAppointment = mock(AppointmentDTO.class, RETURNS_DEEP_STUBS);
        final Service mockService = mock(Service.class);
        final Business mockBusiness = mock(Business.class);
        final Employee employee = mock(Employee.class);
        final User client = mock(User.class);


        mockGetAppointment(client, employee, mockService, mockBusiness);

        appointmentService.add(mockAppointment, 1, 1);
    }

    @Test(expected = EmployeeNotWorkingException.class)
    public void addShouldFailBecauseEmployeeDoesNotHaveShiftSpecified() {
        AppointmentDTO mockAppointmentDTO = new AppointmentDTO();
        final Employee mockEmployee = mock(Employee.class);
        final Service mockService = mock(Service.class);
        final Business mockBusiness = mock(Business.class);
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

        //employee is busy during appointment time
        when(mockEmployee.isAvailable(any(), any(), any())).thenReturn(false);

        mockGetAppointment(client, mockEmployee, mockService, mockBusiness);

        appointmentService.add(mockAppointmentDTO, 1, 1);
    }
//
//    @Test(expected = EmployeeAppointmentConflictException.class)
//    public void addShouldFailBecauseEmployeeIsBookedAlready() {
//        final Appointment mockAppointment = mock(Appointment.class, RETURNS_DEEP_STUBS);
//        final Employee mockEmployee = mock(Employee.class);
//
//        when(mockAppointment.getEmployee()).thenReturn(mockEmployee);
//        when(mockAppointment.getService().getEmployees().contains(any(Employee.class))).thenReturn(true);
//        when(mockEmployee.isWorking(any(), any(), any())).thenReturn(true);
//
//        final List<Appointment> conflictingEmployeeAppointments = Collections.singletonList(createMockedConflictingAppointment());
//        when(appointmentRepository.findByDateAndEmployeeIdAndStatus(any(), anyLong(), any())).thenReturn(conflictingEmployeeAppointments);
//
//        appointmentService.add(mockAppointment);
//    }
//
//    @Test(expected = ClientAppointmentConflictException.class)
//    public void addShouldFailBecauseClientIsBookedAlready() {
//        final Appointment mockAppointment = mock(Appointment.class, RETURNS_DEEP_STUBS);
//        final Employee mockEmployee = mock(Employee.class);
//
//        when(mockAppointment.getEmployee()).thenReturn(mockEmployee);
//        when(mockAppointment.getService().getEmployees().contains(any(Employee.class))).thenReturn(true);
//        when(mockEmployee.isWorking(any(), any(), any())).thenReturn(true);
//
//        final List<Appointment> nonConflictingEmployeeAppointments = Collections.singletonList(createMockedNonConflictingAppointment());
//        when(appointmentRepository.findByDateAndEmployeeIdAndStatus(any(), anyLong(), any())).thenReturn(nonConflictingEmployeeAppointments);
//
//        final List<Appointment> conflictingClientAppointments = Collections.singletonList(createMockedConflictingAppointment());
//        when(appointmentRepository.findByDateAndClientIdAndStatus(any(), anyLong(), any())).thenReturn(conflictingClientAppointments);
//
//        appointmentService.add(mockAppointment);
//    }
//
//    @Test(expected = NoRoomAvailableException.class)
//    public void addShouldFailBecauseThereAreNoAvailableRooms() {
//        final Appointment mockAppointment = mock(Appointment.class, RETURNS_DEEP_STUBS);
//        final Employee mockEmployee = mock(Employee.class);
//
//        when(mockAppointment.getEmployee()).thenReturn(mockEmployee);
//        when(mockAppointment.getService().getEmployees().contains(any(Employee.class))).thenReturn(true);
//        when(mockEmployee.isWorking(any(), any(), any())).thenReturn(true);
//
//        final List<Appointment> nonConflictingEmployeeAppointments = Collections.singletonList(createMockedNonConflictingAppointment());
//        when(appointmentRepository.findByDateAndEmployeeIdAndStatus(any(), anyLong(), any())).thenReturn(nonConflictingEmployeeAppointments);
//
//        final List<Appointment> nonConflictingClientAppointments = Collections.singletonList(createMockedNonConflictingAppointment());
//        when(appointmentRepository.findByDateAndClientIdAndStatus(any(), anyLong(), any())).thenReturn(nonConflictingClientAppointments);
//
//        final Appointment conflictingAppointment = mock(Appointment.class, RETURNS_DEEP_STUBS);
//        when(conflictingAppointment.isConflicting(any(Appointment.class))).thenReturn(true);
//        when(appointmentRepository.findByDateAndStatus(any(), any())).thenReturn(Collections.singletonList(conflictingAppointment));
//
//        final Room room1 = mock(Room.class);
//        final Room room2 = mock(Room.class);
//        final Set<Room> roomSet = Sets.newHashSet(room1, room2);
//        final Set<Room> otherRoomSet = Sets.newHashSet(room1, room2);
//
//        when(mockAppointment.getService().getRooms()).thenReturn(roomSet);
//        when(conflictingAppointment.getService().getRooms()).thenReturn(otherRoomSet);
//
//        appointmentService.add(mockAppointment);
//    }
//
//    @Test
//    public void addPassesWithNoConflictingAppointments() {
//        final Appointment mockAppointment = mock(Appointment.class, RETURNS_DEEP_STUBS);
//        final Employee mockEmployee = mock(Employee.class);
//
//        when(mockAppointment.getEmployee()).thenReturn(mockEmployee);
//        when(mockAppointment.getService().getEmployees().contains(any(Employee.class))).thenReturn(true);
//        when(mockEmployee.isWorking(any(), any(), any())).thenReturn(true);
//
//        final List<Appointment> nonConflictingEmployeeAppointments = Collections.singletonList(createMockedNonConflictingAppointment());
//        when(appointmentRepository.findByDateAndEmployeeIdAndStatus(any(), anyLong(), any())).thenReturn(nonConflictingEmployeeAppointments);
//
//        final List<Appointment> nonConflictingClientAppointments = Collections.singletonList(createMockedNonConflictingAppointment());
//        when(appointmentRepository.findByDateAndClientIdAndStatus(any(), anyLong(), any())).thenReturn(nonConflictingClientAppointments);
//
//        final List<Appointment> nonConflictingAppointments = Collections.singletonList(createMockedNonConflictingAppointment());
//        when(appointmentRepository.findByDateAndStatus(any(), any())).thenReturn(nonConflictingAppointments);
//
//        final Appointment expectedResult = mock(Appointment.class);
//        when(appointmentRepository.save(mockAppointment)).thenReturn(expectedResult);
//
//        final Appointment conflictingAppointment = createMockedConflictingAppointment();
//        when(appointmentRepository.findByDateAndStatus(any(), any())).thenReturn(Collections.singletonList(conflictingAppointment));
//
//        final Room room1 = mock(Room.class);
//        final Room room2 = mock(Room.class);
//        final Set<Room> roomSet = Sets.newHashSet(room1, room2);
//        final Set<Room> otherRoomSet = Sets.newHashSet(room1);
//
//        when(mockAppointment.getService().getRooms()).thenReturn(roomSet);
//        when(conflictingAppointment.getService().getRooms()).thenReturn(otherRoomSet);
//
//        assertEquals(expectedResult, appointmentService.add(mockAppointment));
//
//        verify(appointmentRepository).save(mockAppointment);
//    }
//
//    @Test(expected = ResourceNotFoundException.class)
//    public void updateAppointmentFailNotFound() {
//        final Appointment mockAppointment = mock(Appointment.class, RETURNS_DEEP_STUBS);
//
//        when(appointmentRepository.findByIdAndClientId(1, 1)).thenReturn(Optional.empty());
//        appointmentService.update(1, 1, mockAppointment);
//    }
//
//    @Test
//    public void updateAppointmentSuccess() {
//        final Appointment mockAppointment = mock(Appointment.class, RETURNS_DEEP_STUBS);
//        final Employee mockEmployee = mock(Employee.class);
//
//        when(mockAppointment.getEmployee()).thenReturn(mockEmployee);
//        when(mockAppointment.getService().getEmployees().contains(any(Employee.class))).thenReturn(true);
//        when(mockEmployee.isWorking(any(), any(), any())).thenReturn(true);
//
//        final List<Appointment> nonConflictingEmployeeAppointments = Collections.singletonList(createMockedNonConflictingAppointment());
//        when(appointmentRepository.findByDateAndEmployeeIdAndStatus(any(), anyLong(), any())).thenReturn(nonConflictingEmployeeAppointments);
//
//        final List<Appointment> nonConflictingClientAppointments = Collections.singletonList(createMockedNonConflictingAppointment());
//        when(appointmentRepository.findByDateAndClientIdAndStatus(any(), anyLong(), any())).thenReturn(nonConflictingClientAppointments);
//
//        final List<Appointment> nonConflictingAppointments = Collections.singletonList(createMockedNonConflictingAppointment());
//        when(appointmentRepository.findByDateAndStatus(any(), any())).thenReturn(nonConflictingAppointments);
//
//        final Appointment expectedResult = mock(Appointment.class);
//        when(appointmentRepository.save(mockAppointment)).thenReturn(expectedResult);
//
//        final Appointment conflictingAppointment = createMockedConflictingAppointment();
//        when(appointmentRepository.findByDateAndStatus(any(), any())).thenReturn(Collections.singletonList(conflictingAppointment));
//
//        final Room room1 = mock(Room.class);
//        final Room room2 = mock(Room.class);
//        final Set<Room> roomSet = Sets.newHashSet(room1, room2);
//        final Set<Room> otherRoomSet = Sets.newHashSet(room1);
//
//        when(mockAppointment.getService().getRooms()).thenReturn(roomSet);
//        when(conflictingAppointment.getService().getRooms()).thenReturn(otherRoomSet);
//
//        when(appointmentRepository.findByIdAndClientId(1, 1)).thenReturn(Optional.of(mockAppointment));
//        when(appointmentRepository.save(mockAppointment)).thenReturn(expectedResult);
//
//        assertEquals(expectedResult, appointmentService.update(1, 1, mockAppointment));
//
//        verify(appointmentRepository).save(mockAppointment);
//    }
//
//    private Appointment createMockedConflictingAppointment() {
//        final Appointment mockedAppointment = mock(Appointment.class, RETURNS_DEEP_STUBS);
//        when(mockedAppointment.isConflicting(any(Appointment.class))).thenReturn(true);
//
//        return mockedAppointment;
//    }
//
//    private Appointment createMockedNonConflictingAppointment() {
//        final Appointment mockedAppointment = mock(Appointment.class, RETURNS_DEEP_STUBS);
//        when(mockedAppointment.isConflicting(any(Appointment.class))).thenReturn(false);
//
//        return mockedAppointment;
//    }
//
//    @Ignore
//    @Test
//    public void findById() {
//        long testId = 1;
//        Appointment appointment = this.appointmentService.findMyAppointmentById(testId, testId);
//        System.out.print("");
//
//    }
//
//    @Test
//    public void findByEmployeeId() {
//        List<Appointment> appointments = new ArrayList<>();
//        for (int i = 0; i < 6; i++) {
//            appointments.add(mock(Appointment.class));
//        }
//        when(appointmentRepository.findByEmployeeId(anyLong())).thenReturn(appointments);
//        List<Appointment> result = this.appointmentService.findByEmployeeId(Long.valueOf(4));
//        assertEquals(6, result.size());
//    }
}
