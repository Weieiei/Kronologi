package appointmentscheduler.dto.business;

import java.time.LocalTime;

public class BusinessHoursDTO {
    public String day;
    public LocalTime openHour;
    public LocalTime closeHour;


    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public LocalTime getOpenHour() {
        return openHour;
    }

    public void setOpenHour(LocalTime openHour) {
        this.openHour = openHour;
    }

    public LocalTime getCloseHour() {
        return closeHour;
    }

    public void setCloseHour(LocalTime closeHour) {
        this.closeHour = closeHour;
    }
}
