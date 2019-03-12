package appointmentscheduler.repository;

import appointmentscheduler.entity.employee_service.EmployeeService;
import appointmentscheduler.entity.user.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeServiceRepository extends JpaRepository<EmployeeService, Long> {

    List<Employee> findByServiceId(long serviceId);


}
