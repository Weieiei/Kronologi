package appointmentscheduler.util;

import appointmentscheduler.entity.event.Event;
import appointmentscheduler.entity.event.EventComparer;

import java.time.LocalTime;
import java.util.List;

public final class DateConflictChecker {
    public static boolean hasConflictList(List<? extends Event> events, Event newEvent) {
        return hasConflictList(events, newEvent, false);
    }

    public static boolean hasConflictList(List<? extends Event> events, Event newEvent, boolean modify) {
        Event currentEvent;
        EventComparer eventComparer = new EventComparer();

        for(int i = 0; i < events.size();i++) {
            currentEvent = events.get(i);
            //if events equal then there is a conflict with the events
            //if modification then can reset to same time
            if(eventComparer.compare(currentEvent, newEvent) == 0 && !(modify && currentEvent.getId() == newEvent.getId())) {
                return true;
            }
        }
        return false;
    }

}
