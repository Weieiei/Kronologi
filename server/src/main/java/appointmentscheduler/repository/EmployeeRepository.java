package appointmentscheduler.repository;

import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
