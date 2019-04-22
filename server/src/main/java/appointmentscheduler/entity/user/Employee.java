package appointmentscheduler.entity.user;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.employee_service.EmployeeService;
import appointmentscheduler.entity.event.AppEvent;
import appointmentscheduler.entity.event.AppEventBase;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.exception.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Employee extends User {

    @OneToMany(mappedBy = "employee",  fetch=FetchType.EAGER)
    Set<EmployeeService> services = new HashSet<>();


    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    Business business;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "shift_id")
    private Set<Shift> shifts = new HashSet<>();


    public Set<EmployeeService> getServices() {
        return services;
    }

    public void addService(Service service) {
        EmployeeService employeeService = new EmployeeService();
        employeeService.setService(service);
        services.add(employeeService);
    }

    public void createService(Service service, Business business) {
        EmployeeService temp = new EmployeeService(business, this, service);
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

    public boolean hasService(Service desiredService) {
        for (EmployeeService employeeService : getServices()) {
            if(employeeService.getService().getId() == desiredService.getId()){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks to see if an appointment can be added. Any of the exceptions can be thrown if validation fails.
     *
     * @param appointment The appointment to validate.
     * @throws ModelValidationException             If the client and employee are the same person.
     * @throws EmployeeDoesNotOfferServiceException If the employee is not assigned to the service specified.
     * @throws EmployeeNotWorkingException          If the employee does not have a shift on the date specified.
     * @throws EmployeeAppointmentConflictException If the employee is already booked on the date and time specified.
     */
    public void validateAndAddAppointment(Appointment appointment) throws ModelValidationException, EmployeeDoesNotOfferServiceException, EmployeeNotWorkingException, EmployeeAppointmentConflictException{
        Shift availableShift;

        // Make sure the client and employee are not the same
        if (appointment.getClient() != null && appointment.getClient().equals(this)) {
            throw new ModelValidationException("You cannot book an appointment with yourself.");
        }

        // Make sure the employee can perform the service requested
        boolean employeeCanDoService = hasService(appointment.getService());


        if (!employeeCanDoService) {
            throw new EmployeeDoesNotOfferServiceException("The employee does not perform that service.");
        }

        // Check if the employee is working on the date specified
        availableShift = isAvailable(appointment);
        if (availableShift == null) {
            throw new EmployeeNotWorkingException("The employee does not have a shift.");
        }

        //Try adding appointment to shift, if false then existing appointment conflicting due to pas check
        if (!availableShift.addAppointment(appointment)){
            throw new EmployeeAppointmentConflictException("There is a conflicting appointment already booked with that employee.");
        }

    }

    //Get shift for appointment
    public Shift isAvailable(AppEvent appEvent) {
        Set<Shift> shifts = getShifts();

        for (final Shift shift : shifts) {
            //get shift that encapsulates appointment
            if (shift.isWithin(appEvent)) {
                return shift;
            }
        }

        return null;
    }

    public Set<AppEventBase> getEmployeeAvailabilities(long duration) {
        Set<AppEventBase> employeeAvailabilities = new HashSet<>();
        Set<Shift> shifts = getShifts();

        //get availabilities from each shift
        for (final Shift shift : shifts) {
            employeeAvailabilities.addAll(shift.getAvailabilities(duration));
        }

        return employeeAvailabilities;
    }

    public boolean removeAppointment(Appointment appointment) {
        Set<Shift> shifts = getShifts();

        for (final Shift shift : shifts) {
            //get shift that encapsulates appointment
            if (shift.isWithin(appointment)) {
                return shift.rempveAppointment(appointment);
            }
        }
        return false;
    }

    public void addShift(Shift shift){
        shifts.add(shift);
    }
}
