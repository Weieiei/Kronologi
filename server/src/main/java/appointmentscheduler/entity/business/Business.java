package appointmentscheduler.entity.business;

import appointmentscheduler.entity.AuditableEntity;
import appointmentscheduler.entity.user.User;

import javax.persistence.*;

@Entity
@Table(name = "business")
public class Business extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "domain")
    private String domain;

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User business_owner;

    public Business() {
    }

    public Business(String name, String domain, String description) {
        this.name = name;
        this.domain = domain;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getOwner(){return this.business_owner;}

    public void setOwner(User user){this.business_owner = user;}
}
