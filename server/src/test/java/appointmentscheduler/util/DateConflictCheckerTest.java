package appointmentscheduler.util;

import appointmentscheduler.entity.event.AppEvent;
import appointmentscheduler.entity.event.AppEventBase;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DateConflictCheckerTest {

    @Test
    public void noConflictTest() {
        List<AppEvent> appEvents = new ArrayList<>();
        AppEventBase newEvent = new AppEventBase(LocalTime.of(4,0), LocalTime.of(5,0), LocalDate.of(2018,1,1));

        appEvents.add(new AppEventBase(LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1,1)));
        appEvents.add(new AppEventBase(LocalTime.of(2,0), LocalTime.of(3,0), LocalDate.of(2018,1,1)));
        appEvents.add(new AppEventBase(LocalTime.of(3,0), LocalTime.of(4,0), LocalDate.of(2018,1,1)));
        assertFalse(DateConflictChecker.hasConflictList(appEvents,newEvent));
    }

    @Test
    public void conflictTest() {
        List<AppEvent> appEvents = new ArrayList<>();
        AppEventBase newEvent = new AppEventBase(LocalTime.of(1,45), LocalTime.of(5,0), LocalDate.of(2018,1,1));

        appEvents.add(new AppEventBase(LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1,1)));
        appEvents.add(new AppEventBase(LocalTime.of(2,0), LocalTime.of(3,0), LocalDate.of(2018,1,1)));
        appEvents.add(new AppEventBase(LocalTime.of(3,0), LocalTime.of(4,0), LocalDate.of(2018,1,1)));
        assertTrue(DateConflictChecker.hasConflictList(appEvents,newEvent));
    }

    @Test
    public void noConflictModify() {
        List<AppEvent> appEvents = new ArrayList<>();
        AppEventBase newEvent = new AppEventBase(0, LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1,1));

        appEvents.add(new AppEventBase(0, LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1,1)));
        appEvents.add(new AppEventBase(LocalTime.of(2,0), LocalTime.of(3,0), LocalDate.of(2018,1,1)));
        appEvents.add(new AppEventBase(LocalTime.of(3,0), LocalTime.of(4,0), LocalDate.of(2018,1,1)));
        assertFalse(DateConflictChecker.hasConflictList(appEvents,newEvent, true));
    }

    @Test
    public void conflictTestModify() {
        List<AppEvent> appEvents = new ArrayList<>();
        AppEventBase newEvent = new AppEventBase(0,LocalTime.of(1,45), LocalTime.of(2,39), LocalDate.of(2018,1,1));

        appEvents.add(new AppEventBase(0, LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1,1)));
        appEvents.add(new AppEventBase(1, LocalTime.of(2,0), LocalTime.of(3,0), LocalDate.of(2018,1,1)));
        appEvents.add(new AppEventBase(LocalTime.of(3,0), LocalTime.of(4,0), LocalDate.of(2018,1,1)));
        assertTrue(DateConflictChecker.hasConflictList(appEvents,newEvent, true));
    }

    @Test
    public void noConflictSeveralTest() {
        List<AppEvent> appEvents = new ArrayList<>();
        List<AppEvent> newAppEvents = new ArrayList<>();

        appEvents.add(new AppEventBase(LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1,1)));
        appEvents.add(new AppEventBase(LocalTime.of(2,0), LocalTime.of(3,0), LocalDate.of(2018,1,1)));
        appEvents.add(new AppEventBase(LocalTime.of(3,0), LocalTime.of(4,0), LocalDate.of(2018,1,1)));

        newAppEvents.add(new AppEventBase(LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1,2)));
        newAppEvents.add(new AppEventBase(LocalTime.of(2,0), LocalTime.of(3,0), LocalDate.of(2018,1,3)));
        newAppEvents.add(new AppEventBase(LocalTime.of(3,0), LocalTime.of(4,0), LocalDate.of(2018,1,4)));

        assertFalse(DateConflictChecker.hasConflictSeveralEvents(appEvents, newAppEvents));
    }

    @Test
    public void conflictEndSeveralTest() {
        List<AppEvent> appEvents = new ArrayList<>();
        List<AppEvent> newAppEvents = new ArrayList<>();

        appEvents.add(new AppEventBase(LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1,1)));
        appEvents.add(new AppEventBase(LocalTime.of(2,0), LocalTime.of(3,0), LocalDate.of(2018,1,1)));
        appEvents.add(new AppEventBase(LocalTime.of(3,0), LocalTime.of(4,0), LocalDate.of(2018,1,1)));

        newAppEvents.add(new AppEventBase(LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1,2)));
        newAppEvents.add(new AppEventBase(LocalTime.of(2,0), LocalTime.of(3,0), LocalDate.of(2018,1,3)));
        newAppEvents.add(new AppEventBase(LocalTime.of(3,0), LocalTime.of(4,0), LocalDate.of(2018,1,1)));

        assertTrue(DateConflictChecker.hasConflictSeveralEvents(appEvents, newAppEvents));
    }

    @Test
    public void conflictMiddleSeveralTest() {
        List<AppEvent> appEvents = new ArrayList<>();
        List<AppEvent> newAppEvents = new ArrayList<>();

        appEvents.add(new AppEventBase(LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1,1)));
        appEvents.add(new AppEventBase(LocalTime.of(2,0), LocalTime.of(3,0), LocalDate.of(2018,1,1)));
        appEvents.add(new AppEventBase(LocalTime.of(3,0), LocalTime.of(4,0), LocalDate.of(2018,1,1)));

        newAppEvents.add(new AppEventBase(LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1,2)));
        newAppEvents.add(new AppEventBase(LocalTime.of(2,0), LocalTime.of(3,0), LocalDate.of(2018,1,1)));
        newAppEvents.add(new AppEventBase(LocalTime.of(3,0), LocalTime.of(4,0), LocalDate.of(2018,1,4)));

        assertTrue(DateConflictChecker.hasConflictSeveralEvents(appEvents, newAppEvents));
    }

    @Test
    public void conflictStartSeveralTest() {
        List<AppEvent> appEvents = new ArrayList<>();
        List<AppEvent> newAppEvents = new ArrayList<>();

        appEvents.add(new AppEventBase(LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1,1)));
        appEvents.add(new AppEventBase(LocalTime.of(2,0), LocalTime.of(3,0), LocalDate.of(2018,1,1)));
        appEvents.add(new AppEventBase(LocalTime.of(3,0), LocalTime.of(4,0), LocalDate.of(2018,1,1)));

        newAppEvents.add(new AppEventBase(LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1,1)));
        newAppEvents.add(new AppEventBase(LocalTime.of(2,0), LocalTime.of(3,0), LocalDate.of(2018,1,2)));
        newAppEvents.add(new AppEventBase(LocalTime.of(3,0), LocalTime.of(4,0), LocalDate.of(2018,1,4)));

        assertTrue(DateConflictChecker.hasConflictSeveralEvents(appEvents, newAppEvents));
    }

}