package appointmentscheduler.entity.user;

import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.shift.Shift;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Employee extends User {

    @JoinTable(
            name = "employee_services",
            joinColumns = { @JoinColumn(name = "employee_id") },
            inverseJoinColumns = { @JoinColumn(name = "service_id") }
    )
    @ManyToMany
    private Set<Service> services;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    private Set<Shift> shifts;

    public Set<Service> getServices() {
        return services;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
    }

    public Set<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(Set<Shift> shifts) {
        this.shifts = shifts;
    }
}
