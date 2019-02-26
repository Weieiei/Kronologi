package appointmentscheduler.entity.service;

import appointmentscheduler.entity.AuditableEntity;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.employee_service.employee_service;
import appointmentscheduler.entity.user.Employee;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "services")
public class Service extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "duration")
    private int duration;


    @OneToMany
    Set<employee_service> employees = new HashSet<>();

//    @JoinTable(
//            name = "employee_services",
//            joinColumns = { @JoinColumn(name = "service_id") },
//            inverseJoinColumns = { @JoinColumn(name = "employee_id") }
//    )
//    @ManyToMany(fetch = FetchType.LAZY)
//    private List<Employee> employees;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = true)
    private Business business;

    public Service() { }

    public Service(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

    public Service(String name, int duration, Business business) {
        this.name = name;
        this.duration = duration;
        this.business = business;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Set<employee_service> getEmployees() {
        return employees;
    }

    public void setEmployee(Employee employee) {
        employee_service temp = new employee_service(business, employee, this);
        this.employees.add(temp);
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }
}
