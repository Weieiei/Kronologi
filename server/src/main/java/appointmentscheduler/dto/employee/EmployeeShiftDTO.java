package appointmentscheduler.dto.employee;

import java.time.LocalDate;
import java.time.LocalTime;

public class EmployeeShiftDTO {

    private long employeeId;
    private int year;
    private int month;
    private int day;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;

    public long getEmployeeId() {
        return employeeId;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public LocalDate getDate() {
        return LocalDate.of(this.getYear(), this.getMonth(), this.getDay());
    }

    public LocalTime getStartTime() {
        return LocalTime.of(this.getStartHour(), this.getStartMinute());
    }

    public LocalTime getEndTime() {
        return LocalTime.of(this.getEndHour(), this.getEndMinute());
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }
}
