package appointmentscheduler.entity.event;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.*;

public class AppEventComparerTest {

    private EventComparer eventComparer;
    private AppEventBase event1;
    private AppEventBase event2;

    @Before
    public void setup() {
        eventComparer = new EventComparer();
    }

    @Test
    public void compareLessThanDay() {
        event1 = new AppEventBase(LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1, 2));
        event2 = new AppEventBase(LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1, 3));
        int result = eventComparer.compare(event1,event2);
        //events not conflicting
        assertFalse(0 == result);
        //event 1 less than event 2
        assertEquals(-1, result);
    }

    @Test
    public void compareGreaterThanDay() {
        event1 = new AppEventBase(LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1, 3));
        event2 = new AppEventBase(LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1, 2));
        int result = eventComparer.compare(event1,event2);
        //events not conflicting
        assertFalse(0 == result);
        //event 2 less than event 1
        assertEquals(1, result);
    }

    @Test
    public void compareLessThanHour() {
        event1 = new AppEventBase(LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1, 2));
        event2 = new AppEventBase(LocalTime.of(2,0), LocalTime.of(3,0), LocalDate.of(2018,1, 2));
        int result = eventComparer.compare(event1,event2);
        //events not conflicting
        assertFalse(0 == result);
        //event 1 less than event 2
        assertEquals(-1, result);
    }

    @Test
    public void compareGreaterThanHour() {
        event1 = new AppEventBase(LocalTime.of(2,0), LocalTime.of(3,0), LocalDate.of(2018,1, 2));
        event2 = new AppEventBase(LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1, 2));
        int result = eventComparer.compare(event1,event2);
        //events not conflicting
        assertFalse(0 == result);
        //event 2 less than event 1
        assertEquals(1, result);
    }

    @Test
    public void compareLessThanMinute() {
        event1 = new AppEventBase(LocalTime.of(1,0), LocalTime.of(1,58), LocalDate.of(2018,1, 2));
        event2 = new AppEventBase(LocalTime.of(1,59), LocalTime.of(2,0), LocalDate.of(2018,1, 2));
        int result = eventComparer.compare(event1,event2);
        //events not conflicting
        assertFalse(0 == result);
        //event 1 less than event 2
        assertEquals(-1, result);
    }

    @Test
    public void compareGreaterThanMinute() {
        event1 = new AppEventBase(LocalTime.of(1,59), LocalTime.of(2,0), LocalDate.of(2018,1, 2));
        event2 = new AppEventBase(LocalTime.of(1,0), LocalTime.of(1,58), LocalDate.of(2018,1, 2));
        int result = eventComparer.compare(event1,event2);
        //events not conflicting
        assertFalse(0 == result);
        //event 2 less than event 1
        assertEquals(1, result);
    }

    @Test
    public void compareSameTimeEvent() {
        event1 = new AppEventBase(LocalTime.of(1,0), LocalTime.of(1,58), LocalDate.of(2018,1, 2));
        event2 = new AppEventBase(LocalTime.of(1,0), LocalTime.of(1,58), LocalDate.of(2018,1, 2));
        int result = eventComparer.compare(event1,event2);
        //events conflicting
        assertEquals(0, result);
    }

    @Test
    public void compareConflictStart() {
        event1 = new AppEventBase(LocalTime.of(1,0), LocalTime.of(1,58), LocalDate.of(2018,1, 2));
        event2 = new AppEventBase(LocalTime.of(1,45), LocalTime.of(1,58), LocalDate.of(2018,1, 2));
        int result = eventComparer.compare(event1,event2);
        //events conflicting
        assertEquals(0, result);
    }

    @Test
    public void compareConflictEnd() {
        event1 = new AppEventBase(LocalTime.of(1,57), LocalTime.of(1,59), LocalDate.of(2018,1, 2));
        event2 = new AppEventBase(LocalTime.of(1,45), LocalTime.of(1,58), LocalDate.of(2018,1, 2));
        int result = eventComparer.compare(event1,event2);
        //events conflicting
        assertEquals(0, result);
    }

    @Test
    public void compareConflictInside() {
        event1 = new AppEventBase(LocalTime.of(1,46), LocalTime.of(1,55), LocalDate.of(2018,1, 2));
        event2 = new AppEventBase(LocalTime.of(1,45), LocalTime.of(1,58), LocalDate.of(2018,1, 2));
        int result = eventComparer.compare(event1,event2);
        //events conflicting
        assertEquals(0, result);
    }

    @Test
    public void compareConflictInsideSecond() {
        event1 = new AppEventBase(LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1, 2));
        event2 = new AppEventBase(LocalTime.of(1,45), LocalTime.of(1,58), LocalDate.of(2018,1, 2));
        int result = eventComparer.compare(event1,event2);
        //events conflicting
        assertEquals(0, result);
    }
}