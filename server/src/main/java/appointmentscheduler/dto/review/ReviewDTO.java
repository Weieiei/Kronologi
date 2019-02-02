package appointmentscheduler.dto.review;

public class ReviewDTO {

    private String content;
    private long clientId;
    private long employeeId;
    private long serviceId;
    private long appointmentId;

    public ReviewDTO() {
    }

    public ReviewDTO(String content, long clientId, long employeeId, long serviceId, long appointmentId) {
        this.content = content;
        this.clientId = clientId;
        this.employeeId = employeeId;
        this.serviceId = serviceId;
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

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(long appointmentId) {
        this.appointmentId = appointmentId;
    }
}
