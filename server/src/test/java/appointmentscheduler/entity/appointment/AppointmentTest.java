package appointmentscheduler.entity.appointment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AppointmentTest {

    @Mock
    private Appointment mockAppointment;

    private LocalDate date;

    @Before
    public void setup() {
        date = LocalDate.of(2019, Month.FEBRUARY, 27);
        when(mockAppointment.getDate()).thenReturn(date);
        when(mockAppointment.getStartTime()).thenReturn(LocalTime.of(14, 0));
        when(mockAppointment.getEndTime()).thenReturn(LocalTime.of(15, 0));
        when(mockAppointment.isConflicting(any(Appointment.class))).thenCallRealMethod();
    }

    @Test
    public void isNotConflictingSameDateBeforeAnother() {
        final Appointment nonTouchingAppointment = mock(Appointment.class);
        when(nonTouchingAppointment.getDate()).thenReturn(date);
        when(nonTouchingAppointment.getStartTime()).thenReturn(LocalTime.of(13, 58));
        when(nonTouchingAppointment.getEndTime()).thenReturn(LocalTime.of(13, 59));

        assertFalse(mockAppointment.isConflicting(nonTouchingAppointment));

        final Appointment touchingAppointment = mock(Appointment.class);
        when(touchingAppointment.getDate()).thenReturn(date);
        when(touchingAppointment.getStartTime()).thenReturn(LocalTime.of(13, 59));
        when(touchingAppointment.getEndTime()).thenReturn(LocalTime.of(14, 0));

        assertFalse(mockAppointment.isConflicting(touchingAppointment));
    }

    @Test
    public void isNotConflictingSameDateAfterAnother() {
        final Appointment nonTouchingAppointment = mock(Appointment.class);
        when(nonTouchingAppointment.getDate()).thenReturn(date);
        when(nonTouchingAppointment.getStartTime()).thenReturn(LocalTime.of(15, 0, 1));
        when(nonTouchingAppointment.getEndTime()).thenReturn(LocalTime.of(16, 0));

        assertFalse(mockAppointment.isConflicting(nonTouchingAppointment));

        final Appointment touchingAppointment = mock(Appointment.class);
        when(touchingAppointment.getDate()).thenReturn(date);
        when(touchingAppointment.getStartTime()).thenReturn(LocalTime.of(15, 0));
        when(touchingAppointment.getEndTime()).thenReturn(LocalTime.of(16, 0));

        assertFalse(mockAppointment.isConflicting(touchingAppointment));
    }

    @Test
    public void isNotConflictingDifferentDate() {
        final Appointment differentDate = mock(Appointment.class);

        when(differentDate.getDate()).thenReturn(LocalDate.of(2019, Month.FEBRUARY, 26));

        assertFalse(mockAppointment.isConflicting(differentDate));
    }

    @Test
    public void isConflictingBeforeWithOverlap() {
        final Appointment conflictTailOverlap = mock(Appointment.class);
        when(conflictTailOverlap.getDate()).thenReturn(date);
        when(conflictTailOverlap.getStartTime()).thenReturn(LocalTime.of(12, 0));
        when(conflictTailOverlap.getEndTime()).thenReturn(LocalTime.of(14, 20));

        assertTrue(mockAppointment.isConflicting(conflictTailOverlap));
    }

    @Test
    public void isConflictingAfterWithOverlap() {
        final Appointment conflictHeadOverlap = mock(Appointment.class);
        when(conflictHeadOverlap.getDate()).thenReturn(date);
        when(conflictHeadOverlap.getStartTime()).thenReturn(LocalTime.of(13, 0));
        when(conflictHeadOverlap.getEndTime()).thenReturn(LocalTime.of(15, 20));

        assertTrue(mockAppointment.isConflicting(conflictHeadOverlap));
    }

    @Test
    public void isConflictingInsideAnother() {
        final Appointment conflictInside = mock(Appointment.class);
        when(conflictInside.getDate()).thenReturn(date);
        when(conflictInside.getStartTime()).thenReturn(LocalTime.of(14, 20));
        when(conflictInside.getEndTime()).thenReturn(LocalTime.of(14, 25));

        assertTrue(mockAppointment.isConflicting(conflictInside));
    }
}