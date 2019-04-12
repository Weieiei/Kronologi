package appointmentscheduler.entity.guest;

import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.AuditableEntity;
import appointmentscheduler.entity.phonenumber.PhoneNumber;

import javax.persistence.*;

@Entity
@Table(name = "guests")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Guest extends AuditableEntity {

    @Id
    @GeneratedValue(generator = "guests_id_sequence", strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "guests_id_sequence", sequenceName = "guests_id_seq")
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "verified")
    private boolean verified;

    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            mappedBy = "guest"
    )
    private PhoneNumber phoneNumber;
;
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    public boolean isEqual(Guest guest) {
        return guest != null && guest.getId() == this.getId();
    }

    public long getId() {
        return id;
    }

    public boolean isGuest() {
        return role.toString().equals("GUEST");
    }

    public void setRole(String role) {
        this.role =  RoleEnum.valueOf(role);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
