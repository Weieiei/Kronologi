package appointmentscheduler.entity.role;

import appointmentscheduler.entity.AuditableEntity;
import appointmentscheduler.entity.business.Business;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private RoleEnum role;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = true)
    private Business business;

    public Role() { }

    public Role(RoleEnum role) {
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

}
