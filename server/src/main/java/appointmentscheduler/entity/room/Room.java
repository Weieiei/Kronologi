package appointmentscheduler.entity.room;

import appointmentscheduler.entity.AuditableEntity;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.service.Service;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "rooms")
public class Room extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", unique = true)
    private String name;

   //TODO add an extra column for business (applies for all other join tables for other entities)
    @JoinTable(
            name = "service_rooms",
            joinColumns = { @JoinColumn(name = "room_id") },
            inverseJoinColumns = { @JoinColumn(name = "service_id") }
    )
    @ManyToMany
    private Set<Service> services;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = true)
    private Business business;

    @Override
    public boolean equals(Object o) {
        return o instanceof Room && this.getId() == ((Room) o).getId();
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

    public Set<Service> getServices() {
        return services;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }
}
