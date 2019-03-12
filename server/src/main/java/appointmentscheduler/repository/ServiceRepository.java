package appointmentscheduler.repository;

import appointmentscheduler.entity.service.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findServicesByBusinessId(long businessId);
    Optional<Service> findByIdAndBusinessId(long id, long businessId);
}
