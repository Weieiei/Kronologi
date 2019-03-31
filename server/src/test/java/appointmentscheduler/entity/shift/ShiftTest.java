package appointmentscheduler.entity.shift;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.event.AppEvent;
import appointmentscheduler.entity.event.AppEventBase;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.*;

public class ShiftTest {

    @Test
    public void isWithinValid() {
        LocalDate date = LocalDate.now();
        LocalTime startTime = LocalTime.of(1,1,1);
        LocalTime endTime = LocalTime.of(2,2,2);

        Shift shift = new Shift();
        AppEvent appEvent = new AppEventBase(startTime, endTime, date);

        shift.setDate(date);
        shift.setStartTime(startTime);
        shift.setEndTime(endTime);

        assertTrue(shift.isWithin(appEvent));
    }

    @Test
    public void isWithinInvalidOverlapWithin() {
        LocalDate date = LocalDate.now();
        LocalTime startTime = LocalTime.of(1,1,1);
        LocalTime endTime = LocalTime.of(8,2,2);

        Shift shift = new Shift();
        AppEvent appEvent = new AppEventBase(startTime.plusHours(1), endTime.minusHours(1), date);

        shift.setDate(date);
        shift.setStartTime(startTime);
        shift.setEndTime(endTime);

        assertTrue(shift.isWithin(appEvent));
    }

    @Test
    public void isWithinInvalidBefore() {
        LocalDate date = LocalDate.now();
        LocalTime startTime = LocalTime.of(1,1,1);
        LocalTime endTime = LocalTime.of(2,2,2);

        Shift shift = new Shift();
        AppEvent appEvent = new AppEventBase(startTime.minusHours(1), startTime, date);

        shift.setDate(date);
        shift.setStartTime(startTime);
        shift.setEndTime(endTime);

        assertFalse(shift.isWithin(appEvent));
    }

    @Test
    public void isWithinInvalidOverlapBefore() {
        LocalDate date = LocalDate.now();
        LocalTime startTime = LocalTime.of(1,1,1);
        LocalTime endTime = LocalTime.of(2,2,2);

        Shift shift = new Shift();
        AppEvent appEvent = new AppEventBase(startTime.minusHours(1), endTime, date);

        shift.setDate(date);
        shift.setStartTime(startTime);
        shift.setEndTime(endTime);

        assertFalse(shift.isWithin(appEvent));
    }

    @Test
    public void isWithinInvalidAfter() {
        LocalDate date = LocalDate.now();
        LocalTime startTime = LocalTime.of(1,1,1);
        LocalTime endTime = LocalTime.of(2,2,2);

        Shift shift = new Shift();
        shift.setDate(date);
        shift.setStartTime(startTime);
        shift.setEndTime(endTime);

        AppEvent appEvent = new AppEventBase(endTime, endTime.plusHours(2), date);


        assertFalse(shift.isWithin(appEvent));
    }

    @Test
    public void isWithinInvalidOverlapAfter() {
        LocalDate date = LocalDate.now();
        LocalTime startTime = LocalTime.of(1,1,1);
        LocalTime endTime = LocalTime.of(2,2,2);

        Shift shift = new Shift();
        AppEvent appEvent = new AppEventBase(startTime, endTime.plusHours(2), date);

        shift.setDate(date);
        shift.setStartTime(startTime);
        shift.setEndTime(endTime);

        assertFalse(shift.isWithin(appEvent));
    }

    @Test
    public void addAppointmentValid() {
        LocalDate date = LocalDate.now();
        LocalTime startTime = LocalTime.of(1,1,1);
        LocalTime endTime = LocalTime.of(2,2,2);

        Shift shift = new Shift();

        Appointment appointment = new Appointment();
        appointment.setDate(date);
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);


        shift.setDate(date);
        shift.setStartTime(startTime);
        shift.setEndTime(endTime);

        assertTrue(shift.addAppointment(appointment));

    }

    @Test
    public void addAppointmentInvalid() {
        LocalDate date = LocalDate.now();
        LocalTime startTime = LocalTime.of(1,1,1);
        LocalTime endTime = LocalTime.of(2,2,2);

        Shift shift = new Shift();

        Appointment appointment = new Appointment();
        appointment.setDate(date);
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);

        Appointment appointmentConflict = new Appointment();
        appointmentConflict.setDate(date);
        appointmentConflict.setStartTime(startTime);
        appointmentConflict.setEndTime(endTime);

        shift.setDate(date);
        shift.setStartTime(startTime);
        shift.setEndTime(endTime);

        shift.addAppointment(appointment);

        assertFalse(shift.addAppointment(appointmentConflict));
    }
}