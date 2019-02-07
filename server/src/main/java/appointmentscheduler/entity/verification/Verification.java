package appointmentscheduler.entity.verification;

import appointmentscheduler.entity.AuditableEntity;
import appointmentscheduler.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Entity
@Table(name = "verification")
public class Verification extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private User user;

    @Column(name = "hash")
    private String hash;

    public Verification() { }

    public Verification(User user) throws NoSuchAlgorithmException {
        this.user = user;
    }

    @PostPersist
    public void afterInsert() throws NoSuchAlgorithmException {
        generateHash();
    }

    private void generateHash() throws NoSuchAlgorithmException {
        String hashtext;
        if(this.user!= null) {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(this.user.getEmail().getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            hashtext = bigInt.toString(16);
            //retrieve hashed text
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
        }
        else
            hashtext = "";
        this.hash = hashtext;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public String getHash() { return hash; }

    public void setHash(String date) { this.hash = date; }

}
