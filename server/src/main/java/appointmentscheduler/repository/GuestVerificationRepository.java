package appointmentscheduler.repository;

import appointmentscheduler.entity.verification.GuestVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestVerificationRepository extends JpaRepository<GuestVerification, Long> {
    String findByGuestId(long guestId);
    GuestVerification findByHash(String hash);
}
