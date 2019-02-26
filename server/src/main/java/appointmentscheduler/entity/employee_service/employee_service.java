package appointmentscheduler.entity.employee_service;

import appointmentscheduler.entity.AuditableEntity;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;

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
    @PrimaryKeyJoinColumn(name="business", referencedColumnName="a")
    private Business businessId;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="employee", referencedColumnName="b")
    private Employee employee;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="service", referencedColumnName="c")
    private Service service;

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
}
