package appointmentscheduler.entity.review;

import appointmentscheduler.entity.AuditableEntity;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "reviews", uniqueConstraints=@UniqueConstraint(columnNames={"id","business_id"}))
public class Review extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @JsonIgnore
    private User client;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    @JsonIgnore
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = true)
    private Business business;



    public Review(){ }

    public Review(String content, Appointment appointment) {
        this.content = content;
        this.client = client;
        this.appointment = appointment;
    }

    public Review(String content, Appointment appointment, Business business ) {
        this.content = content;
        this.client = client;
        this.appointment = appointment;
        this.business = business;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User customer) {
        this.client = customer;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }
}
