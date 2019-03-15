package appointmentscheduler.repository;

import appointmentscheduler.entity.employee_service.EmployeeService;
import appointmentscheduler.entity.googleEntity.SyncEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SyncSettingRepository extends JpaRepository<SyncEntity, Long> {
    Optional<SyncEntity> findByUserId(long userId);
}
