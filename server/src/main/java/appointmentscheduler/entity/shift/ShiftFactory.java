package appointmentscheduler.entity.shift;

import java.time.LocalDate;
import java.time.LocalTime;

public class ShiftFactory {

    public static Shift createShift(LocalDate date, LocalTime startTime, LocalTime endTime) {
        Shift shift = new Shift();

        shift.setDate(date);
        shift.setStartTime(startTime);
        shift.setEndTime(endTime);

        return shift;
    }

}
