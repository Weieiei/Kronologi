package appointmentscheduler.entity.appointment;

import appointmentscheduler.entity.AuditableEntity;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointments")
public class Appointment extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "notes")
    private String notes;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;


    public Appointment() {
    }

    public Appointment(User client, Employee employee, Service service, LocalDate date, LocalTime startTime, String notes) {
        this.client = client;
        this.employee = employee;
        this.service = service;
        this.date = date;
        this.startTime = startTime;
        this.notes = notes;
    }

    public Appointment(User client, Employee employee, Service service, LocalDate date, LocalTime startTime,
                       String notes, Business business) {
        this.client = client;
        this.employee = employee;
        this.service = service;
        this.date = date;
        this.startTime = startTime;
        this.notes = notes;
        this.business = business;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Appointment && ((Appointment) obj).getId() == this.getId();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
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
        if (endTime == null) {
            adjustEndTime();
        }
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    @PrePersist
    public void beforeInsert() {
        adjustEndTime();
        this.status = AppointmentStatus.CONFIRMED;
    }

    @PreUpdate
    public void beforeUpdate() {
        adjustEndTime();
    }

    private void adjustEndTime() {
        this.endTime = startTime.plusMinutes(this.service.getDuration());
    }

    public boolean isConflicting(Appointment appointment) {
        return appointment.getDate().equals(this.getDate()) &&
                !(appointment.getEndTime().isBefore(this.getStartTime()) || appointment.getEndTime().equals(this.getStartTime()) || appointment.getStartTime().isAfter(this.getEndTime()) || appointment.getStartTime().equals(this.getEndTime()));
    }
}
