package appointmentscheduler.entity.verification;

import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;

@Entity(name = "google_credentials")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "google_credentials")
public class GoogleCred {

    @Id
    private String key;

    private String accessToken;
    private Long expirationTimeMilliseconds;
    private String refreshToken;

    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;

    public GoogleCred(String key, StoredCredential credential) {
        this.key = key;
        this.accessToken = credential.getAccessToken();
        this.expirationTimeMilliseconds = credential.getExpirationTimeMilliseconds();
        this.refreshToken = credential.getRefreshToken();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void apply(StoredCredential credential) {
        this.accessToken = credential.getAccessToken();
        this.expirationTimeMilliseconds = credential.getExpirationTimeMilliseconds();
        this.refreshToken = credential.getRefreshToken();
        this.updatedAt = Instant.now();
    }
}
