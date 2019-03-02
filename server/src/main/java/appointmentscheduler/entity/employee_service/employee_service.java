package appointmentscheduler.entity.employee_service;

import appointmentscheduler.entity.AuditableEntity;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "employee_service")
public class employee_service extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="business", referencedColumnName="id")
    private Business businessId;

    @JsonBackReference
    @ManyToOne
    @PrimaryKeyJoinColumn(name="employee", referencedColumnName="id")
    private Employee employee;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="service_id", referencedColumnName="id")
    private Service service;

    public employee_service(){}
    public employee_service(Business business, Employee employee, Service service){
        this.businessId = business;
        this.employee = employee;
        this.service = service;
    }

    public Service getService(){
        return this.service;
    }

    public void setService(Service service){
        this.service = service;
    }

    public Employee getEmployee(){
        return this.employee;
    }

    public void setEmployee(Service service){
        this.employee = employee;
    }
}
