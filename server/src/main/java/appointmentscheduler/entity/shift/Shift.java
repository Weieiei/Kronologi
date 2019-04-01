package appointmentscheduler.entity.shift;

import appointmentscheduler.entity.AuditableEntity;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.event.AppEvent;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.event.AppEventBase;
import appointmentscheduler.entity.event.EventComparer;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.exception.ModelValidationException;
import appointmentscheduler.util.DateConflictChecker;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import static java.time.temporal.ChronoUnit.MINUTES;
import java.util.*;

@Entity
@Table(name = "employee_shifts")
public class Shift extends AuditableEntity implements AppEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Employee employee;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "appointment_id")
    private Set<Appointment> appointments;

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;


    public Shift() {
        appointments = new HashSet<>();
    }

    public Shift(Employee employee, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.employee = employee;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        appointments = new HashSet<>();
    }

    public Shift(Business business, Employee employee, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.employee = employee;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.business = business;
        appointments = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }

    public boolean isWithin(AppEvent appEvent) {
        LocalDate appDate = appEvent.getDate();
        LocalTime appStartTime = appEvent.getStartTime();
        LocalTime appEndTime = appEvent.getEndTime();

        if (date.equals(appDate) && // check if same date
                (startTime.isBefore(appStartTime) || startTime.equals(appStartTime)) && // check if shift start time <= app start time
                (endTime.isAfter(appEndTime) || endTime.equals(appEndTime)) // check if shift end time >= app end time
        ) {
            return true;
        }
        return false;
    }

    public boolean addAppointment(Appointment appointment){
        //check if appointment is within shift and doesn't conflict with past appointments in shift
        if(isWithin(appointment) && !DateConflictChecker.hasConflictSet(this.appointments, appointment, false)){
            appointments.add(appointment);
            return true;
        }

        return false;
    }

    public boolean rempveAppointment(Appointment appointment) {
        return appointments.remove(appointment);
    }

    public Set<AppEventBase> getAvailabilities(long duration) {
        long diffMinute;
        Set<AppEventBase> availabilities = new HashSet<>();
        AppEventBase availability;
        Appointment appointment;
        Appointment nextAppointment;

        //sort appointments by precedence
        EventComparer eventComparer = new EventComparer();
        List<Appointment> sortedAppointments = new ArrayList<>(appointments);
        Collections.sort(sortedAppointments, eventComparer);

        //shifts can only be on one day
        LocalDate date = this.date;

        for(int i = 0; i < sortedAppointments.size(); i++){
            appointment = sortedAppointments.get(i);
            //availability between start and first appointment
            if(i == 0 && appointment.getStartTime().isAfter(this.startTime)) {
                diffMinute = MINUTES.between(this.startTime, appointment.getStartTime());
                if(diffMinute >= duration) {
                    availability = new AppEventBase(startTime, startTime.plusMinutes(diffMinute), date);
                    availabilities.add(availability);
                }
            }
            //availability between last appointment and shift end
            if(i == sortedAppointments.size() - 1 && appointment.getEndTime().isBefore(this.endTime)){
                diffMinute = MINUTES.between(appointment.getEndTime(), this.endTime);
                if(diffMinute >= duration) {
                    availability = new AppEventBase(appointment.getEndTime(), appointment.getEndTime().plusMinutes(diffMinute), date);
                    availabilities.add(availability);
                }
            }
            //availability between appointments
            if (i != sortedAppointments.size() - 1 && 1 != 0) {
                nextAppointment = sortedAppointments.get(i+1);
                diffMinute = MINUTES.between(appointment.getEndTime(), nextAppointment.getStartTime());
                if(diffMinute >= duration) {
                    availability = new AppEventBase(appointment.getEndTime(), appointment.getEndTime().plusMinutes(diffMinute), date);
                    availabilities.add(availability);
                }
            }
        }

        return availabilities;
    }

    @PrePersist
    public void beforeInsert() {
        validateTimes();
    }

    @PreUpdate
    public void beforeUpdate() {
        validateTimes();
    }

    private void validateTimes() {
        if (getEndTime().isBefore(getStartTime())) {
            throw new ModelValidationException("A shift's start time should be before its end time.");
        }
    }

}
