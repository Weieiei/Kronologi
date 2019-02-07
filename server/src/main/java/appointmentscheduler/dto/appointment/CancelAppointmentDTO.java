package appointmentscheduler.dto.appointment;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CancelAppointmentDTO {

    @JsonProperty("idOfAppointment")
    private long idOfAppointment;

    @JsonProperty("reason")
    private String reason;

    private long userWhoCancelled;

    public CancelAppointmentDTO() {
    }

    public CancelAppointmentDTO(String reason, long idOfAppointment) {
        this.reason = reason;
        this.idOfAppointment = idOfAppointment;
    }


    public long getIdOfCancelledAppointment() {
        return idOfAppointment;
    }

    public void setIdOfCancelledAppointment(long idOfCancelledAppointment) {
        this.idOfAppointment = idOfCancelledAppointment;
    }

    public long getIdPersonWhoCancelled(){ return userWhoCancelled; }

    public void setIdPersonWhoCancelled(long id){ this.userWhoCancelled = id;}

    public String getCancelReason(){ return reason;}

    public void  setCancelReason(String cancelReason){ this.reason = cancelReason;}

}
