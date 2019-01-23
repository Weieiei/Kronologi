package appointmentscheduler.repository;

import appointmentscheduler.entity.phonenumber.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {

    List<PhoneNumber> findByUserId(long userId);

}
