package appointmentscheduler.entity.event;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.*;

public class EventComparerTest {

    private EventComparer eventComparer;

    @Before
    public void setup() {
        eventComparer = new EventComparer();
    }
    @Test
    public void compare() {
        EventTest event1 = new EventTest(LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1, 2));
        EventTest event2 = new EventTest(LocalTime.of(1,0), LocalTime.of(2,0), LocalDate.of(2018,1, 3));
        int result = eventComparer.compare(event1,event2);
        //events not conflicting
        assertFalse(0 == result);
        //event 1 less than event 2
        assertEquals(-1, result);
    }
}