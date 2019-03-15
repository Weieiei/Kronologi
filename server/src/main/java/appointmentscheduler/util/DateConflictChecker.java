package appointmentscheduler.util;

import appointmentscheduler.entity.event.Event;
import appointmentscheduler.entity.event.EventComparer;

import java.time.LocalTime;
import java.util.List;

public final class DateConflictChecker {
    public static boolean hasConflictList(List<? extends Event> events, Event newEvent) {
        Event currentEvent;
        EventComparer eventComparer = new EventComparer();

        for(int i = 0; i < events.size();i++) {
            currentEvent = events.get(i);
            //if events equal then there is a conflict with the events
            if(eventComparer.compare(currentEvent, newEvent) == 0) {
                return true;
            }
        }
        return false;
    }
}
