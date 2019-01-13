package appointmentscheduler.entity.room;

import appointmentscheduler.entity.AuditableEntity;
import appointmentscheduler.entity.service.Service;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "rooms")
public class Room extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @JoinTable(
            name = "service_rooms",
            joinColumns = { @JoinColumn(name = "room_id") },
            inverseJoinColumns = { @JoinColumn(name = "service_id") }
    )
    @ManyToMany
    private Set<Service> services;

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
}
