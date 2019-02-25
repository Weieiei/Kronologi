package appointmentscheduler.entity.user;

import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.shift.Shift;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
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

    public boolean isWorking(LocalDate date, LocalTime startTime, LocalTime endTime) {
        final Set<Shift> shifts = getShifts();

        for (final Shift shift : shifts) {
            final LocalTime shiftStartTime = shift.getStartTime();
            final LocalTime shiftEndTime = shift.getEndTime();

            if (shift.getDate().equals(date) && // check if same date
                    (shiftStartTime.isBefore(startTime) || shiftStartTime.equals(startTime)) && // check if shift start time <= start time
                    (shiftEndTime.isAfter(endTime) || shiftEndTime.equals(endTime)) // check if shift end time >= end time
            ) {
                return true;
            }
        }

        return false;
    }
}
