package appointmentscheduler.repository;

import appointmentscheduler.entity.verification.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {
    String findByUserId(long userId);
}
