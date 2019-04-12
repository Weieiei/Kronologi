package appointmentscheduler.repository;

import appointmentscheduler.entity.file.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserFileRepository extends JpaRepository<UserFile, Long> {
    Optional<UserFile> findByIdAndUserId(long fileId, long userId);
    //UserFile findByUserId(long userId);
    Optional<UserFile>  findByUserId(long userId);

}
