package appointmentscheduler.dto.appointment;

public class CancelAppointmentDTO {

    private long businessId;
    private long appointmentId;
    private long userWhoCancelled;
    private String reason;

    public CancelAppointmentDTO() {
    }

    public CancelAppointmentDTO(String reason, long appointmentId, long businessId) {
        this.reason = reason;
        this.appointmentId = appointmentId;
        this.businessId = businessId;
    }


    public long getIdOfCancelledAppointment() {
        return appointmentId;
    }

    public void setIdOfCancelledAppointment(long idOfCancelledAppointment) {
        this.appointmentId = idOfCancelledAppointment;
    }

    public long getIdPersonWhoCancelled(){ return userWhoCancelled; }

    public void setIdPersonWhoCancelled(long id){ this.userWhoCancelled = id;}

    public String getCancelReason(){ return reason;}

    public void  setCancelReason(String cancelReason){ this.reason = cancelReason;}

    public long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(long businessId) {
        this.businessId = businessId;
    }
}
