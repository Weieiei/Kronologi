package appointmentscheduler.entity.user;

import appointmentscheduler.entity.event.AppEventBase;

import java.util.Set;

public class EmployeeAvailability {
    private Employee employee;
    private Set<AppEventBase> availabilities;

    public EmployeeAvailability(Employee employee, Set<AppEventBase> availabilities) {
        this.employee = employee;
        this.availabilities = availabilities;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Set<AppEventBase> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(Set<AppEventBase> availabilities) {
        this.availabilities = availabilities;
    }
}
