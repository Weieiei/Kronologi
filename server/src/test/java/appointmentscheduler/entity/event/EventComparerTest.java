package appointmentscheduler.entity.event;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.*;

public class EventComparerTest {

    private EventComparer eventComparer;
    private EventTest event1;
    private EventTest event2;

    @Before
    public void setup() {
        eventComparer = new EventComparer();
    }

    @Test
    public void compareLessThanDay() {
        event1 = new EventTest(LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1, 2));
        event2 = new EventTest(LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1, 3));
        int result = eventComparer.compare(event1,event2);
        //events not conflicting
        assertFalse(0 == result);
        //event 1 less than event 2
        assertEquals(-1, result);
    }

    @Test
    public void compareGreaterThanDay() {
        event1 = new EventTest(LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1, 3));
        event2 = new EventTest(LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1, 2));
        int result = eventComparer.compare(event1,event2);
        //events not conflicting
        assertFalse(0 == result);
        //event 2 less than event 1
        assertEquals(1, result);
    }
}