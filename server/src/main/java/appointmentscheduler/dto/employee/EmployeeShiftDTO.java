package appointmentscheduler.dto.employee;

import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.user.Employee;

import java.time.LocalDate;
import java.time.LocalTime;

public class EmployeeShiftDTO {

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Shift convertToShift(Employee employee, Business business){
        Shift shift = new Shift();
        shift.setEmployee(employee);
        shift.setBusiness(business);
        shift.setDate(this.getDate());
        shift.setStartTime(this.getStartTime());
        shift.setEndTime(this.getEndTime());
        return shift;
    }
}
