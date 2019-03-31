package appointmentscheduler.entity.user;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.event.AppEvent;
import appointmentscheduler.entity.event.AppEventTest;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.exception.EmployeeDoesNotOfferServiceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeTest {

    @Mock
    private Employee mockEmployee;

    private Set<Shift> shifts;

    @Before
    public void setup() {
        shifts = createMockShifts();
    }

   @Test
    public void isWorkingSuccess() {
        when(mockEmployee.getShifts()).thenReturn(shifts);
        when(mockEmployee.isAvailable(any(AppEvent.class))).thenCallRealMethod();

        // Date of appointment
        // Feb 27, 12:00 - 13:00
        LocalDate date = LocalDate.of(2019, Month.FEBRUARY, 27);
        LocalTime startTime = LocalTime.of(12, 0);
        LocalTime endTime = LocalTime.of(13, 0);
        AppEvent appEvent = new AppEventTest(startTime, endTime, date);

        Shift result = mockEmployee.isAvailable(appEvent);

        assertNotNull(result);
    }

    @Test
    public void isWorkingWrongDate() {
        when(mockEmployee.getShifts()).thenReturn(shifts);
        when(mockEmployee.isAvailable(any(AppEvent.class))).thenCallRealMethod();

        // Date of appointment
        // Feb 28, 12:00 - 13:00
        LocalDate date = LocalDate.of(2019, Month.FEBRUARY, 28);
        LocalTime startTime = LocalTime.of(12, 0);
        LocalTime endTime = LocalTime.of(13, 0);

        AppEvent appEvent = new AppEventTest(startTime, endTime, date);

        Shift result = mockEmployee.isAvailable(appEvent);

        assertNull(result);
    }

    @Test
    public void isWorkingWrongTimeAfterNoOverlap() {
        when(mockEmployee.getShifts()).thenReturn(shifts);
        when(mockEmployee.isAvailable(any(AppEvent.class))).thenCallRealMethod();

        // Date of appointment
        // Feb 27, 16:00:01 - 17:00
        LocalDate date = LocalDate.of(2019, Month.FEBRUARY, 27);
        LocalTime startTime = LocalTime.of(16, 0, 1);
        LocalTime endTime = LocalTime.of(17, 0);

        AppEvent appEvent = new AppEventTest(startTime, endTime, date);

        Shift result = mockEmployee.isAvailable(appEvent);

        assertNull(result);
    }

    @Test
    public void isWorkingWrongTimeBeforeNoOverlap() {
        when(mockEmployee.getShifts()).thenReturn(shifts);
        when(mockEmployee.isAvailable(any(AppEvent.class))).thenCallRealMethod();

        // Date of appointment
        // Feb 27, 7:00 - 7:59:59
        LocalDate date = LocalDate.of(2019, Month.FEBRUARY, 27);
        LocalTime startTime = LocalTime.of(7, 0);
        LocalTime endTime = LocalTime.of(7, 59, 59);

        AppEvent appEvent = new AppEventTest(startTime, endTime, date);

        Shift result = mockEmployee.isAvailable(appEvent);

        assertNull(result);
    }

    @Test
    public void isWorkingWrongTimeAfterWithOverlap() {
        when(mockEmployee.getShifts()).thenReturn(shifts);
        when(mockEmployee.isAvailable(any(AppEvent.class))).thenCallRealMethod();

        // Date of appointment
        // Feb 27, 15:30 - 16:30
        LocalDate date = LocalDate.of(2019, Month.FEBRUARY, 27);
        LocalTime startTime = LocalTime.of(15, 30);
        LocalTime endTime = LocalTime.of(16, 30);

        AppEvent appEvent = new AppEventTest(startTime, endTime, date);

        Shift result = mockEmployee.isAvailable(appEvent);

        assertNull(result);
    }

    @Test
    public void isWorkingWrongTimeBeforeWithOverlap() {
        when(mockEmployee.getShifts()).thenReturn(shifts);
        when(mockEmployee.isAvailable(any(AppEvent.class))).thenCallRealMethod();

        // Date of appointment
        // Feb 27, 7:00 - 8:30 (1.5 hours)
        LocalDate date = LocalDate.of(2019, Month.FEBRUARY, 27);
        LocalTime startTime = LocalTime.of(7, 0);
        LocalTime endTime = LocalTime.of(8, 30);

        AppEvent appEvent = new AppEventTest(startTime, endTime, date);

        Shift result = mockEmployee.isAvailable(appEvent);

        assertNull(result);
    }

    private Set<Shift> createMockShifts() {
        final Set<Shift> shifts = new HashSet<>();

        // Feb 27, 2019. 8:00 - 16:00
        LocalDate date1 = LocalDate.of(2019, Month.FEBRUARY, 27);
        LocalTime startTime1 = LocalTime.of(8, 0);
        LocalTime endTime1 = LocalTime.of(16, 0);

        // Mar 15, 2019. 8:30 - 16:30
        LocalDate date2 = LocalDate.of(2019, Month.MARCH, 15);
        LocalTime startTime2 = LocalTime.of(8, 30);
        LocalTime endTime2 = LocalTime.of(16, 30);

        // Jan 3, 2019. 12:30 - 20:30
        LocalDate date3 = LocalDate.of(2019, Month.JANUARY, 3);
        LocalTime startTime3 = LocalTime.of(12, 30);
        LocalTime endTime3 = LocalTime.of(20, 30);

        shifts.add( new Shift(mockEmployee, date1, startTime1, endTime1));
        shifts.add(new Shift(mockEmployee,date2, startTime2, endTime2));
        shifts.add(new Shift(mockEmployee,date3, startTime3, endTime3));

        mockEmployee.setShifts(shifts);

        return shifts;
    }

    @Test(expected = EmployeeDoesNotOfferServiceException.class)
    public void addShouldFailBecauseEmployeeDoesNotOfferService() {
        final Appointment mockAppointment = mock(Appointment.class);
        final Service mockService = mock(Service.class);
        final Service mockRetrievedService = mock(Service.class);
        Employee employee = new Employee();
        User client = new User();

        employee.setId(0);
        client.setId(1);
        when(mockAppointment.getClient()).thenReturn(client);

        when(mockService.getId()).thenReturn((long)1);
        employee.setEmployeeServices(Arrays.asList(mockService));
        when(mockRetrievedService.getId()).thenReturn((long)2);
        when(mockAppointment.getService()).thenReturn(mockRetrievedService);

        employee.validateAndAddAppointment(mockAppointment);
    }
}