package appointmentscheduler.dto.employee;

import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.event.AppEvent;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.user.Employee;

import java.time.LocalDate;
import java.time.LocalTime;

public class EmployeeShiftDTO implements AppEvent {

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;


    public EmployeeShiftDTO() {
    }

    public EmployeeShiftDTO(LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public long getId() {
        //No id since not yet stored in database
        return -1;
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
