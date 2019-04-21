package appointmentscheduler.entity.verification;

import appointmentscheduler.entity.AuditableEntity;
import appointmentscheduler.entity.guest.Guest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Entity
@Table(name = "guest_verification")
public class GuestVerification extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id", nullable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private Guest guest;

    @Column(name = "hash")
    private String hash;

    public GuestVerification() { }

    public GuestVerification(Guest guest) {
        this.guest = guest;
    }

    @PostPersist
    public void afterInsert()  {
        generateHash();
    }

    private void generateHash()  {
        try {
            String hashtext;
            if (this.guest != null) {
                MessageDigest m = MessageDigest.getInstance("SHA-256");
                m.reset();
                m.update(this.guest.getEmail().getBytes());
                BigInteger bigInt = new BigInteger(1, m.digest());
                hashtext = bigInt.toString(16);
                while (hashtext.length() < 32) {
                    hashtext = "0" + hashtext;
                }

            } else
                hashtext = "";
            this.hash = hashtext;
        }
        catch (NoSuchAlgorithmException e){
            System.err.println("SHA-256 is not a valid message digest algorithm");
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String date) {
        this.hash = date;
    }

}
