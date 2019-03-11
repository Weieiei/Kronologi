package appointmentscheduler.repository;

import appointmentscheduler.entity.file.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByIdAndBusinessId(long fileId, long businessId);
}
