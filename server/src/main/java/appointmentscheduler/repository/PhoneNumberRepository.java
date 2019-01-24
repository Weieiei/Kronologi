package appointmentscheduler.repository;

import appointmentscheduler.entity.phonenumber.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {

    Optional<PhoneNumber> findByUserId(long userId);

}
