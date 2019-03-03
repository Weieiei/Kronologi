package appointmentscheduler.entity.user;

import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.employee_service.employee_service;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.shift.Shift;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Employee extends User {

    @JsonManagedReference
    @OneToMany(mappedBy = "employee",  fetch=FetchType.EAGER)
    Set<employee_service> services = new HashSet<>();


    @ManyToOne
    @JoinColumn(name = "business_id", nullable = true)
    Business business;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Shift> shifts;


    public Set<employee_service> getServices() {
        return services;
    }

    public void createService(Service service){
        employee_service temp = new employee_service(this.business, this, service);
        this.services.add(temp);
    }


    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
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
