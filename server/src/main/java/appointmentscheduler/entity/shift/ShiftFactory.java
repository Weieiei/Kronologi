package appointmentscheduler.entity.shift;

import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.user.Employee;

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

    public static Shift createShift(Employee employee, LocalDate date, LocalTime startTime, LocalTime endTime) {
        Shift shift = createShift(date, startTime, endTime);
        shift.setEmployee(employee);

        return shift;
    }


    public static Shift createShift(Business business, Employee employee, LocalDate date, LocalTime startTime,
                                    LocalTime endTime) {
        Shift shift = createShift(date, startTime, endTime);
        shift.setEmployee(employee);
        shift.setBusiness(business);

        return shift;
    }

}
