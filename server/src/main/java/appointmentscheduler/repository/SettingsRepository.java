package appointmentscheduler.repository;

import appointmentscheduler.entity.settings.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, Long> {

    Optional<Settings> findByUserId(long userId);

}
