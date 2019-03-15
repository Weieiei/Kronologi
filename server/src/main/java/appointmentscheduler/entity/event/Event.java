package appointmentscheduler.entity.event;

import java.time.LocalDate;
import java.time.LocalTime;

public interface Event {

     LocalTime getStartTime();
     LocalTime getEndTime();
     LocalDate getDate();
}
