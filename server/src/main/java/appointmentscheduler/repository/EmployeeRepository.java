package appointmentscheduler.repository;

import appointmentscheduler.entity.user.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByShifts_Date(LocalDate date);

    //TODO REFACTOR TO JPQL AND NOT NATIVEQUERY
    @Query(value = "select DISTINCT * from users u INNER JOIN employee_service emp ON emp.employee_id = u.id INNER JOIN employee_shifts shift ON shift.employee_id = u.id where emp.service_id = :#{#serviceId} AND u.dtype = 'Employee' AND shift.date = :#{#date}",nativeQuery = true)
    List<Employee> findByServices_IdAndShifts_Date(@Param("serviceId") long serviceId, @Param("date") LocalDate date);

    List<Employee>  findByBusinessId(long businessId);

    Optional<Employee> findByIdAndBusinessId(long employeeId, long businessId);
}
