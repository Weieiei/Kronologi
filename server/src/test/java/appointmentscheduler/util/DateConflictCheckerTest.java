package appointmentscheduler.util;

import appointmentscheduler.entity.event.Event;
import appointmentscheduler.entity.event.EventTest;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DateConflictCheckerTest {

    @Test
    public void noConflictTest() {
        List<Event> events = new ArrayList<>();
        EventTest newEvent = new EventTest(LocalTime.of(4,0), LocalTime.of(5,0), LocalDate.of(2018,1,1));

        events.add(new EventTest(LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1,1)));
        events.add(new EventTest(LocalTime.of(2,0), LocalTime.of(3,0), LocalDate.of(2018,1,1)));
        events.add(new EventTest(LocalTime.of(3,0), LocalTime.of(4,0), LocalDate.of(2018,1,1)));
        assertFalse(DateConflictChecker.hasConflictList(events,newEvent));
    }

    @Test
    public void conflictTest() {
        List<Event> events = new ArrayList<>();
        EventTest newEvent = new EventTest(LocalTime.of(1,45), LocalTime.of(5,0), LocalDate.of(2018,1,1));

        events.add(new EventTest(LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1,1)));
        events.add(new EventTest(LocalTime.of(2,0), LocalTime.of(3,0), LocalDate.of(2018,1,1)));
        events.add(new EventTest(LocalTime.of(3,0), LocalTime.of(4,0), LocalDate.of(2018,1,1)));
        assertTrue(DateConflictChecker.hasConflictList(events,newEvent));
    }

    @Test
    public void noConflictModify() {
        List<Event> events = new ArrayList<>();
        EventTest newEvent = new EventTest(0, LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1,1));

        events.add(new EventTest(0, LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1,1)));
        events.add(new EventTest(LocalTime.of(2,0), LocalTime.of(3,0), LocalDate.of(2018,1,1)));
        events.add(new EventTest(LocalTime.of(3,0), LocalTime.of(4,0), LocalDate.of(2018,1,1)));
        assertFalse(DateConflictChecker.hasConflictList(events,newEvent, true));
    }

    @Test
    public void conflictTestModify() {
        List<Event> events = new ArrayList<>();
        EventTest newEvent = new EventTest(0,LocalTime.of(1,45), LocalTime.of(2,39), LocalDate.of(2018,1,1));

        events.add(new EventTest(0, LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1,1)));
        events.add(new EventTest(1, LocalTime.of(2,0), LocalTime.of(3,0), LocalDate.of(2018,1,1)));
        events.add(new EventTest(LocalTime.of(3,0), LocalTime.of(4,0), LocalDate.of(2018,1,1)));
        assertTrue(DateConflictChecker.hasConflictList(events,newEvent, true));
    }
}