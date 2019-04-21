package appointmentscheduler.repository;

import appointmentscheduler.entity.file.ServiceFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceFileRepository extends JpaRepository<ServiceFile, Long> {
    Optional<ServiceFile> findByIdAndServiceId(long fileId, long serviceId);

    Optional<ServiceFile>  findByServiceId(long serviceId);


}
