package appointmentscheduler.dto.review;

public class ReviewDTO {

    private String content;
    private long appointmentId;
    private long clientId;

    public ReviewDTO() {
    }

    public ReviewDTO(String content, long clientId, long employeeId, long serviceId, long appointmentId) {
        this.content = content;
        this.clientId = clientId;
        this.appointmentId = appointmentId;
    }

    public ReviewDTO(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

  public long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(long appointmentId) {
        this.appointmentId = appointmentId;
    }
}
