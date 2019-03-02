package appointmentscheduler.repository;

import appointmentscheduler.entity.shift.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    List<Shift> findByDate(LocalDate date);
    List<Shift> findByEmployeeId(long id);
    Optional<Shift> findByEmployeeIdAndDate(long employeeId, LocalDate date);
    List<Shift> findByIdAndBusinessId(long employeeId, long businessId);
}
