package appointmentscheduler.entity.service;

import appointmentscheduler.entity.AuditableEntity;

import javax.persistence.*;

@Entity
@Table(name = "services")
public class ServiceEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "duration")
    private int duration;

    public ServiceEntity() { }

    public ServiceEntity(String name, int duration) {
        this.name = name;
        this.duration = duration;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}
