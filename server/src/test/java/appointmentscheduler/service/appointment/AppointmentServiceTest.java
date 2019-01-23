package appointmentscheduler.service.appointment;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.exception.EmployeeDoesNotOfferServiceException;
import appointmentscheduler.exception.EmployeeNotWorkingException;
import appointmentscheduler.exception.ModelValidationException;
import appointmentscheduler.repository.AppointmentRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    private AppointmentService appointmentService;

    @Before
    public void setup() {
        appointmentService = new AppointmentService(appointmentRepository);
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

        // Apparently we don't need to stub this
        // when(mockAppointment.getService().getEmployees().contains(any(Employee.class))).thenReturn(false);

        appointmentService.add(mockAppointment);
    }

    @Test(expected = EmployeeNotWorkingException.class)
    public void addShouldFailBecauseEmployeeDoesNotHaveShiftSpecified() {
        final Appointment mockAppointment = mock(Appointment.class, RETURNS_DEEP_STUBS);
        final Employee mockEmployee = mock(Employee.class);

        when(mockAppointment.getEmployee()).thenReturn(mockEmployee);
        when(mockAppointment.getService().getEmployees().contains(any(Employee.class))).thenReturn(true);
        // Apparently we don't need to stub this
        // when(mockEmployee.isWorking(any(LocalDate.class), any(LocalTime.class), any(LocalTime.class))).thenReturn(false);

        appointmentService.add(mockAppointment);
    }
}