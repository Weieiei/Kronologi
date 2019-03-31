package appointmentscheduler.entity.business;

import appointmentscheduler.entity.AuditableEntity;
import appointmentscheduler.entity.file.File;
import appointmentscheduler.entity.user.User;

import javax.persistence.*;
import java.util.Set;

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

    @OneToMany(mappedBy = "business",  fetch=FetchType.EAGER)
    private Set<BusinessHours> businessHours;

    @Column(name = "image")
    private String businessLogo;

    @Column(name = "address")
    private String formattedAddress;

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

    public void setBusinessHours(Set<BusinessHours> businessHours){this.businessHours = businessHours;}

    public Set<BusinessHours> getBusinessHours(){return this.businessHours;}

    public void setAddress(String address){this.formattedAddress = address;}

    public String getAddress(){return this.formattedAddress;}

    public String getBusinessLogo() {
        return businessLogo;
    }

    public void setBusinessLogo(String businessLogo) {
        this.businessLogo = businessLogo;
    }
}
