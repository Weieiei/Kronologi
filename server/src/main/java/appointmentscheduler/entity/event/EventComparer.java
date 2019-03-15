package appointmentscheduler.entity.event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;

public final class EventComparer implements Comparator<Event> {
    @Override
    public int compare(Event o1, Event o2) {
        LocalDate d1 = o1.getDate();
        LocalDate d2 = o2.getDate();

        LocalTime s1 = o1.getStartTime();
        LocalTime s2 = o2.getStartTime();

        LocalTime e1 = o1.getEndTime();
        LocalTime e2 = o2.getEndTime();

        if(d1.isEqual(d2)) {
            //Events equal if there is overlap between them between start and end time
            if((s1.isBefore(e2) && (s1.isAfter(s2) || s1.equals(s2)))
                    || (e1.isAfter(s2) && (e1.isBefore(e2) || e1.equals(e2)))
                    || (s1.isBefore(s2) || s1.equals(s2)) && (e1.isAfter(e2) || e1.equals(e2))) {
                return 0;
            }
            //no overlap so only need to check start time
            else if(s1.isBefore(s2)) {
                return -1;
            } else {
                return 1;
            }

        } else if(d1.isBefore(d2)) {
            //less than
            return -1;
        }
        else {
            return 1;
        }
    }


}
