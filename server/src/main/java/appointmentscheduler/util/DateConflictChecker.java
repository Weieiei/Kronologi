package appointmentscheduler.util;

import appointmentscheduler.entity.event.Event;
import appointmentscheduler.entity.event.EventComparer;

import java.time.LocalTime;
import java.util.Collections;
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

    public static boolean hasConflictSeveralEvents(List<? extends Event> events, List<? extends Event> newEvents) {
        EventComparer eventComparer = new EventComparer();
        Event currentEvent;
        int currentIndex = 0;
        Event currentNewEvent;
        int newIndex = 0;
        int comparison;
        //sort event lists, sorting allows conflict checking to be done in O(n) instead of O(n^2)
        Collections.sort(events, eventComparer);
        Collections.sort(newEvents, eventComparer);

        while (true) {
            currentEvent = events.get(currentIndex);
            currentNewEvent = newEvents.get(newIndex);
            comparison = eventComparer.compare(currentNewEvent, currentEvent);
            if(comparison == -1) {
                //last new event is before current old event
                if(newIndex >= newEvents.size() - 1) {
                    return false;
                } else {
                    newIndex++;
                }
            } else if(comparison == 1) {
                //last old event is before current new event
                if(currentIndex >= events.size() - 1) {
                    return false;
                } else {
                    currentIndex++;
                }
            } else
                return true;
        }
    }

}
