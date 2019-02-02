package appointmentscheduler.service.appointment;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.room.Room;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.exception.*;
import appointmentscheduler.repository.AppointmentRepository;
import appointmentscheduler.repository.EmployeeRepository;
import appointmentscheduler.repository.ShiftRepository;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ShiftRepository shiftRepository;

    private AppointmentService appointmentService;

    @Before
    public void setup() {
        appointmentService = new AppointmentService(appointmentRepository, employeeRepository, shiftRepository);
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

        final Appointment mockAppointment = mock(Appointment.class);

        when(mockAppointment.getClient()).thenReturn(client);
        when(mockAppointment.getEmployee()).thenReturn(employee);

        appointmentService.add(mockAppointment);
    }

    @Test(expected = EmployeeDoesNotOfferServiceException.class)
    public void addShouldFailBecauseEmployeeDoesNotOfferService() {
        final Appointment mockAppointment = mock(Appointment.class, RETURNS_DEEP_STUBS);

        appointmentService.add(mockAppointment);
    }

    @Test(expected = EmployeeNotWorkingException.class)
    public void addShouldFailBecauseEmployeeDoesNotHaveShiftSpecified() {
        final Appointment mockAppointment = mock(Appointment.class, RETURNS_DEEP_STUBS);
        final Employee mockEmployee = mock(Employee.class);

        when(mockAppointment.getEmployee()).thenReturn(mockEmployee);
        when(mockAppointment.getService().getEmployees().contains(any(Employee.class))).thenReturn(true);

        appointmentService.add(mockAppointment);
    }

    @Test(expected = EmployeeAppointmentConflictException.class)
    public void addShouldFailBecauseEmployeeIsBookedAlready() {
        final Appointment mockAppointment = mock(Appointment.class, RETURNS_DEEP_STUBS);
        final Employee mockEmployee = mock(Employee.class);

        when(mockAppointment.getEmployee()).thenReturn(mockEmployee);
        when(mockAppointment.getService().getEmployees().contains(any(Employee.class))).thenReturn(true);
        when(mockEmployee.isWorking(any(), any(), any())).thenReturn(true);

        final List<Appointment> conflictingEmployeeAppointments = Collections.singletonList(createMockedConflictingAppointment());
        when(appointmentRepository.findByDateAndEmployeeIdAndStatus(any(), anyLong(), any())).thenReturn(conflictingEmployeeAppointments);

        appointmentService.add(mockAppointment);
    }

    @Test(expected = ClientAppointmentConflictException.class)
    public void addShouldFailBecauseClientIsBookedAlready() {
        final Appointment mockAppointment = mock(Appointment.class, RETURNS_DEEP_STUBS);
        final Employee mockEmployee = mock(Employee.class);

        when(mockAppointment.getEmployee()).thenReturn(mockEmployee);
        when(mockAppointment.getService().getEmployees().contains(any(Employee.class))).thenReturn(true);
        when(mockEmployee.isWorking(any(), any(), any())).thenReturn(true);

        final List<Appointment> nonConflictingEmployeeAppointments = Collections.singletonList(createMockedNonConflictingAppointment());
        when(appointmentRepository.findByDateAndEmployeeIdAndStatus(any(), anyLong(), any())).thenReturn(nonConflictingEmployeeAppointments);

        final List<Appointment> conflictingClientAppointments = Collections.singletonList(createMockedConflictingAppointment());
        when(appointmentRepository.findByDateAndClientIdAndStatus(any(), anyLong(), any())).thenReturn(conflictingClientAppointments);

        appointmentService.add(mockAppointment);
    }

    @Test(expected = NoRoomAvailableException.class)
    public void addShouldFailBecauseThereAreNoAvailableRooms() {
        final Appointment mockAppointment = mock(Appointment.class, RETURNS_DEEP_STUBS);
        final Employee mockEmployee = mock(Employee.class);

        when(mockAppointment.getEmployee()).thenReturn(mockEmployee);
        when(mockAppointment.getService().getEmployees().contains(any(Employee.class))).thenReturn(true);
        when(mockEmployee.isWorking(any(), any(), any())).thenReturn(true);

        final List<Appointment> nonConflictingEmployeeAppointments = Collections.singletonList(createMockedNonConflictingAppointment());
        when(appointmentRepository.findByDateAndEmployeeIdAndStatus(any(), anyLong(), any())).thenReturn(nonConflictingEmployeeAppointments);

        final List<Appointment> nonConflictingClientAppointments = Collections.singletonList(createMockedNonConflictingAppointment());
        when(appointmentRepository.findByDateAndClientIdAndStatus(any(), anyLong(), any())).thenReturn(nonConflictingClientAppointments);

        final Appointment conflictingAppointment = mock(Appointment.class, RETURNS_DEEP_STUBS);
        when(conflictingAppointment.isConflicting(any(Appointment.class))).thenReturn(true);
        when(appointmentRepository.findByDateAndStatus(any(), any())).thenReturn(Collections.singletonList(conflictingAppointment));

        final Room room1 = mock(Room.class);
        final Room room2 = mock(Room.class);
        final Set<Room> roomSet = Sets.newHashSet(room1, room2);
        final Set<Room> otherRoomSet = Sets.newHashSet(room1, room2);

        when(mockAppointment.getService().getRooms()).thenReturn(roomSet);
        when(conflictingAppointment.getService().getRooms()).thenReturn(otherRoomSet);

        appointmentService.add(mockAppointment);
    }

    @Test
    public void addPassesWithNoConflictingAppointments() {
        final Appointment mockAppointment = mock(Appointment.class, RETURNS_DEEP_STUBS);
        final Employee mockEmployee = mock(Employee.class);

        when(mockAppointment.getEmployee()).thenReturn(mockEmployee);
        when(mockAppointment.getService().getEmployees().contains(any(Employee.class))).thenReturn(true);
        when(mockEmployee.isWorking(any(), any(), any())).thenReturn(true);

        final List<Appointment> nonConflictingEmployeeAppointments = Collections.singletonList(createMockedNonConflictingAppointment());
        when(appointmentRepository.findByDateAndEmployeeIdAndStatus(any(), anyLong(), any())).thenReturn(nonConflictingEmployeeAppointments);

        final List<Appointment> nonConflictingClientAppointments = Collections.singletonList(createMockedNonConflictingAppointment());
        when(appointmentRepository.findByDateAndClientIdAndStatus(any(), anyLong(), any())).thenReturn(nonConflictingClientAppointments);

        final List<Appointment> nonConflictingAppointments = Collections.singletonList(createMockedNonConflictingAppointment());
        when(appointmentRepository.findByDateAndStatus(any(), any())).thenReturn(nonConflictingAppointments);

        final Appointment expectedResult = mock(Appointment.class);
        when(appointmentRepository.save(mockAppointment)).thenReturn(expectedResult);

        final Appointment conflictingAppointment = createMockedConflictingAppointment();
        when(appointmentRepository.findByDateAndStatus(any(), any())).thenReturn(Collections.singletonList(conflictingAppointment));

        final Room room1 = mock(Room.class);
        final Room room2 = mock(Room.class);
        final Set<Room> roomSet = Sets.newHashSet(room1, room2);
        final Set<Room> otherRoomSet = Sets.newHashSet(room1);

        when(mockAppointment.getService().getRooms()).thenReturn(roomSet);
        when(conflictingAppointment.getService().getRooms()).thenReturn(otherRoomSet);

        assertEquals(expectedResult, appointmentService.add(mockAppointment));

        verify(appointmentRepository).save(mockAppointment);
    }

    private Appointment createMockedConflictingAppointment() {
        final Appointment mockedAppointment = mock(Appointment.class, RETURNS_DEEP_STUBS);
        when(mockedAppointment.isConflicting(any(Appointment.class))).thenReturn(true);

        return mockedAppointment;
    }

    private Appointment createMockedNonConflictingAppointment() {
        final Appointment mockedAppointment = mock(Appointment.class, RETURNS_DEEP_STUBS);
        when(mockedAppointment.isConflicting(any(Appointment.class))).thenReturn(false);

        return mockedAppointment;
    }
}
