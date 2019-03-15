package appointmentscheduler.entity.event;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventTest implements Event {
    //CLass created to test event conflict functionality
    LocalTime startTime;
    LocalTime endTime;
    LocalDate date;

    public EventTest(LocalTime startTime, LocalTime endTime, LocalDate date) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
    }

    @Override
    public LocalTime getStartTime() {
        return startTime;
    }

    @Override
    public LocalTime getEndTime() {
        return endTime;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }
}
