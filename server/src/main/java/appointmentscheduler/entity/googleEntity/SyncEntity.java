package appointmentscheduler.entity.googleEntity;

import appointmentscheduler.entity.AuditableEntity;
import appointmentscheduler.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "google_calendar_sync")
public class SyncEntity extends AuditableEntity {

    @Id
    private int id;

    @OneToOne(fetch = FetchType.EAGER)
    private User user;

    private String syncToken;

    public SyncEntity(String syncToken, User user){
        this.user = user;
        this.syncToken = syncToken;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSyncToken() {
        return syncToken;
    }

    public void setSyncToken(String syncToken) {
        this.syncToken = syncToken;
    }
}
