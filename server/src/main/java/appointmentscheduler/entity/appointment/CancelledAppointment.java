package appointmentscheduler.entity.appointment;

import appointmentscheduler.entity.AuditableEntity;
import appointmentscheduler.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "cancelled_appointment")
public class CancelledAppointment extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_of_appointment", nullable = false)
    private Appointment appointment;

    @Column(name = "cancel_reason")
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private User canceller;

    public CancelledAppointment() { }

    public CancelledAppointment(User canceller, String reason, Appointment appointment) {
        this.appointment = appointment;
        this.canceller = canceller;
        this.reason = reason;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public User getCanceller() {
        return canceller;
    }

    public void setCanceller(User canceller) {
        this.canceller = canceller;
    }

}
