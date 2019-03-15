package appointmentscheduler.entity.event;

import java.time.LocalDate;
import java.time.LocalTime;

public interface Event {

     long getId();
     LocalTime getStartTime();
     LocalTime getEndTime();
     LocalDate getDate();
}
