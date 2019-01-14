package appointmentscheduler.entity.shift;

import appointmentscheduler.entity.AuditableEntity;
import appointmentscheduler.exception.ModelValidationException;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "employee_shifts")
public class Shift extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
