package appointmentscheduler.entity.phonenumber;

import appointmentscheduler.entity.AuditableEntity;
import appointmentscheduler.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "phone_numbers")
public class PhoneNumber extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "area_code")
    private String areaCode;

    @Column(name = "number")
    private String number;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    public PhoneNumber() { }

    public PhoneNumber(String countryCode, String areaCode, String number) {
        this.countryCode = countryCode;
        this.areaCode = areaCode;
        this.number = number;
    }

    public PhoneNumber(String countryCode, String areaCode, String number, User user) {
        this(countryCode, areaCode, number);
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRawPhoneNumber() {
        return countryCode + areaCode + number;
    }

}
