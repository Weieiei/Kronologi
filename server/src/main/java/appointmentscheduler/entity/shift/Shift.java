package appointmentscheduler.entity.shift;

import appointmentscheduler.entity.AuditableEntity;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.exception.ModelValidationException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "employee_shifts")
public class Shift extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    public Shift() { }

    public Shift(User employee, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.employee = employee;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
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

    public LocalDateTime getStartDateTime() {
        return LocalDateTime.of(getDate(), getStartTime());
    }

    public LocalDateTime getEndDateTime() {
        return LocalDateTime.of(getDate(), getEndTime());
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
        if (getEndDateTime().isBefore(getStartDateTime())) {
            throw new ModelValidationException("A shift's start time should be before its end time.");
        }
    }

}
