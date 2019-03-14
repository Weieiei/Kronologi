package appointmentscheduler.util;

import appointmentscheduler.entity.Event;

import java.time.LocalTime;
import java.util.List;

public final class DateConflictChecker {
    public static boolean hasConflictList(List<? extends Event> events, Event newEvent) {
        Event currentEvent;
        LocalTime eventStart;
        LocalTime eventEnd;
        LocalTime currentStart;
        LocalTime currentEnd;

        for(int i = 0; i < events.size();i++) {
            currentEvent = events.get(i);
            if(newEvent.getDate().isEqual(currentEvent.getDate())){
                eventStart = newEvent.getStartTime();
                eventEnd = newEvent.getEndTime();
                currentStart = currentEvent.getStartTime();
                currentEnd =currentEvent.getEndTime();

                if((eventStart.isBefore(currentEnd) && (eventStart.isAfter(currentStart) || eventStart.equals(currentStart)))
                        || (eventEnd.isAfter(currentStart) && (eventEnd.isBefore(currentEnd) || eventEnd.equals(currentEnd)))
                        || (eventStart.isBefore(currentStart) || eventStart.equals(currentStart)) && (eventEnd.isAfter(currentEnd) || eventEnd.equals(currentEnd)))
                    return true;
            }
        }
        return false;
    }
}
