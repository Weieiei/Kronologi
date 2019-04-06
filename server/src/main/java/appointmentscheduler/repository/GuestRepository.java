package appointmentscheduler.repository;

import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.guest.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long> {
}
