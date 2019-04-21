package appointmentscheduler.entity.settings;

import appointmentscheduler.entity.AuditableEntity;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "user_settings")
public class Settings extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private boolean emailReminder;

    @Column
    private boolean textReminder;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = true)
    private Business business;

    public Settings() {
    }

    public Settings(boolean emailReminder, boolean textReminder, User user) {
        this.emailReminder = emailReminder;
        this.textReminder = textReminder;
        this.user = user;
    }

    public Settings(boolean emailReminder, boolean textReminder, User user, Business business) {
        this.emailReminder = emailReminder;
        this.textReminder = textReminder;
        this.user = user;
        this.business = business;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isEmailReminder() {
        return emailReminder;
    }

    public void setEmailReminder(boolean emailReminder) {
        this.emailReminder = emailReminder;
    }

    public boolean isTextReminder() {
        return textReminder;
    }

    public void setTextReminder(boolean textReminder) {
        this.textReminder = textReminder;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }
}
