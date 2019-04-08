package appointmentscheduler.repository;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.guest.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    Optional<Guest> findGuestByIdAndEmailIgnoreCase(long id, String email);
    Optional<Guest> findGuestByEmailIgnoreCase(String email);
    Optional<Guest> findGuestById(long id);
//    List<Guest> findAllGuests();
}
