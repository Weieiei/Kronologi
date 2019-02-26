package appointmentscheduler.entity.user;

import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.employee_service.employee_service;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.shift.Shift;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Employee extends User {


    @OneToMany
    Set<employee_service>  services = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = true)
    Business business;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    private Set<Shift> shifts;

    public Set<employee_service> getServices() {
        return services;
    }

    public void createService(Service service){
        employee_service temp = new employee_service(business, this, service);
        this.services.add(temp);
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
