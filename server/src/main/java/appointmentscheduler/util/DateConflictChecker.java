package appointmentscheduler.util;

import appointmentscheduler.entity.event.AppEvent;
import appointmentscheduler.entity.event.EventComparer;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public final class DateConflictChecker {
    public static boolean hasConflictList(List<? extends AppEvent> events, AppEvent newAppEvent) {
        return hasConflictList(events, newAppEvent, false);
    }

    public static boolean hasConflictList(List<? extends AppEvent> events, AppEvent newAppEvent, boolean modify) {
        AppEvent currentAppEvent;
        EventComparer eventComparer = new EventComparer();

        for(int i = 0; i < events.size();i++) {
            currentAppEvent = events.get(i);
            //if events equal then there is a conflict with the events
            //if modification then can reset to same time
            if(eventComparer.compare(currentAppEvent, newAppEvent) == 0 && !(modify && currentAppEvent.getId() == newAppEvent.getId())) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasConflictSet(Set<? extends AppEvent> events, AppEvent newAppEvent, boolean modify) {
        EventComparer eventComparer = new EventComparer();

        for(AppEvent currentAppEvent: events) {
            //if events equal then there is a conflict with the events
            //if modification then can reset to same time
            if(eventComparer.compare(currentAppEvent, newAppEvent) == 0 && !(modify && currentAppEvent.getId() == newAppEvent.getId())) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasConflictSeveralEvents(List<? extends AppEvent> events, List<? extends AppEvent> newEvents) {
        EventComparer eventComparer = new EventComparer();
        AppEvent currentAppEvent;
        int currentIndex = 0;
        AppEvent currentNewAppEvent;
        int newIndex = 0;
        int comparison;
        //sort event lists, sorting allows conflict checking to be done in O(n) instead of O(n^2)
        Collections.sort(events, eventComparer);
        Collections.sort(newEvents, eventComparer);

        currentAppEvent = events.get(currentIndex);
        currentNewAppEvent = newEvents.get(newIndex);
        while (true) {
            comparison = eventComparer.compare(currentNewAppEvent, currentAppEvent);
            if(comparison == -1) {
                //last new event is before current old event
                if(newIndex >= newEvents.size() - 1) {
                    return false;
                } else {
                    newIndex++;
                    currentNewAppEvent = newEvents.get(newIndex);
                }
            } else if(comparison == 1) {
                //last old event is before current new event
                if(currentIndex >= events.size() - 1) {
                    return false;
                } else {
                    currentIndex++;
                    currentAppEvent = events.get(currentIndex);
                }
            } else
                return true;
        }
    }

}
