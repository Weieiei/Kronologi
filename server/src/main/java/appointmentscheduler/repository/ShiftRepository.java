package appointmentscheduler.repository;

import appointmentscheduler.entity.shift.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    Optional<Shift> findByEmployeeIdAndDate(long employeeId, LocalDate date);
}
