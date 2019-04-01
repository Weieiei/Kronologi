package appointmentscheduler.entity.shift;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.event.AppEvent;
import appointmentscheduler.entity.event.AppEventBase;
import appointmentscheduler.entity.event.EventComparer;
import appointmentscheduler.entity.user.Employee;
import org.junit.Test;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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

    @Test
    public void availabilityAppointmentCoverShift(){
        LocalDate date = LocalDate.now();
        LocalTime startTime = LocalTime.of(1,1,1);
        LocalTime endTime = LocalTime.of(2,2,2);
        Appointment appointment = new Appointment();
        Shift shift = new Shift();
        Set<AppEventBase> avail;

        appointment.setDate(date);
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);

        shift.setDate(date);
        shift.setStartTime(startTime);
        shift.setEndTime(endTime);
        shift.addAppointment(appointment);

        avail = shift.getAvailabilities(30);

        //no availabilitities since appointment covers full shift
        assertTrue(0 == avail.size());
    }

    @Test
    public void availabilityAppointmentStart(){
        LocalDate date = LocalDate.now();
        LocalTime startTime = LocalTime.of(1,1,1);
        LocalTime endTime = LocalTime.of(2,2,2);
        Appointment appointment = new Appointment();
        Shift shift = new Shift();
        Set<AppEventBase> avail;

        appointment.setDate(date);
        appointment.setStartTime(startTime.plusMinutes(30));
        appointment.setEndTime(endTime);

        shift.setDate(date);
        shift.setStartTime(startTime);
        shift.setEndTime(endTime);
        shift.addAppointment(appointment);

        avail = shift.getAvailabilities(30);

        //availability at start of appointment
        assertTrue(1 == avail.size());
        for (AppEventBase appEventBase : avail) {
            assertEquals(startTime, appEventBase.getStartTime());
            assertEquals(startTime.plusMinutes(30), appEventBase.getEndTime());
        }
    }

    @Test
    public void availabilityAppointmentEnd(){
        LocalDate date = LocalDate.now();
        LocalTime startTime = LocalTime.of(1,1,1);
        LocalTime endTime = LocalTime.of(2,2,2);
        Appointment appointment = new Appointment();
        Shift shift = new Shift();
        Set<AppEventBase> avail;

        appointment.setDate(date);
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime.minusMinutes(30));

        shift.setDate(date);
        shift.setStartTime(startTime);
        shift.setEndTime(endTime);
        shift.addAppointment(appointment);

        avail = shift.getAvailabilities(30);

        //availability at start of appointment
        assertEquals(1, avail.size());
        for (AppEventBase appEventBase : avail) {
            assertEquals(endTime.minusMinutes(30), appEventBase.getStartTime());
            assertEquals(endTime, appEventBase.getEndTime());
        }
    }

    @Test
    public void availabilityAppointmentMiddle(){
        LocalDate date = LocalDate.now();
        LocalTime startTime = LocalTime.of(1,1);
        LocalTime endTime = LocalTime.of(2,2);
        Appointment appointment = new Appointment();
        Appointment appointment2 = new Appointment();
        Shift shift = new Shift();
        Set<AppEventBase> avail;

        appointment.setDate(date);
        appointment.setStartTime(startTime);
        appointment.setEndTime(startTime.plusMinutes(15));

        appointment2.setDate(date);
        appointment2.setStartTime(endTime.minusMinutes(15));
        appointment2.setEndTime(endTime);


        shift.setDate(date);
        shift.setStartTime(startTime);
        shift.setEndTime(endTime);

        shift.addAppointment(appointment);
        shift.addAppointment(appointment2);

        avail = shift.getAvailabilities(30);

        //availability at start of appointment
        assertEquals(1, avail.size());
        for (AppEventBase appEventBase : avail) {
            assertEquals(startTime.plusMinutes(15), appEventBase.getStartTime());
            assertEquals(endTime.minusMinutes(15), appEventBase.getEndTime());
        }
    }

    @Test
    public void availabilityAppointmentTwo(){
        LocalDate date = LocalDate.now();
        LocalTime startTime = LocalTime.of(1,0);
        LocalTime endTime = LocalTime.of(3,0);
        Appointment appointment = new Appointment();

        Shift shift = new Shift();
        Set<AppEventBase> avail;

        appointment.setDate(date);
        appointment.setStartTime(startTime.plusMinutes(30));
        appointment.setEndTime(startTime.plusMinutes(45));

        shift.setDate(date);
        shift.setStartTime(startTime);
        shift.setEndTime(endTime);

        shift.addAppointment(appointment);


        avail = shift.getAvailabilities(30);

        assertEquals(2, avail.size());

        EventComparer eventComparer = new EventComparer();
        List<AppEventBase> sortedAvail = new ArrayList<>(avail);
        Collections.sort(sortedAvail, eventComparer);

        assertEquals(startTime, sortedAvail.get(0).getStartTime());
        assertEquals(startTime.plusMinutes(30), sortedAvail.get(0).getEndTime());

        assertEquals(startTime.plusMinutes(45), sortedAvail.get(1).getStartTime());
        assertEquals(endTime, sortedAvail.get(1).getEndTime());

    }

}