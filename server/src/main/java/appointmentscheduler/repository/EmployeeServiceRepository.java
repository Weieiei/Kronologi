package appointmentscheduler.repository;

import appointmentscheduler.entity.employee_service.employee_service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeServiceRepository extends JpaRepository<employee_service, Long> {
}
