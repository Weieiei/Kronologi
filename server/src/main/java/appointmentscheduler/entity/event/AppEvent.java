package appointmentscheduler.entity.event;

import java.time.LocalDate;
import java.time.LocalTime;

public interface AppEvent {

     long getId();
     LocalTime getStartTime();
     LocalTime getEndTime();
     LocalDate getDate();
}
