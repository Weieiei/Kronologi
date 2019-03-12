package appointmentscheduler.repository;

import appointmentscheduler.entity.verification.GoogleCred;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface GoogleCredentialRepository extends JpaRepository<GoogleCred, String> {
    Optional<GoogleCred> findByKey(String key);
    Optional<GoogleCred> findByAccessToken(String key);
    @Query(value = "select key from google_jpa_data_store_credential", nativeQuery = true)
    Set<String> findAllKeys();
}