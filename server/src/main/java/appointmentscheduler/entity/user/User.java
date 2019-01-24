package appointmentscheduler.entity.user;

import appointmentscheduler.entity.AuditableEntity;
import appointmentscheduler.entity.phonenumber.PhoneNumber;
import appointmentscheduler.entity.role.Role;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.settings.Settings;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends AuditableEntity {

    @Id
    @GeneratedValue(generator = "users_id_sequence", strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "users_id_sequence", sequenceName = "users_id_seq")
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<Role> roles;

    @JoinTable(
            name = "employee_services",
            joinColumns = {@JoinColumn(name = "employee_id")},
            inverseJoinColumns = {@JoinColumn(name = "service_id")}
    )
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Service> employeeServices;

    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            mappedBy = "user"
    )
    private PhoneNumber phoneNumber;

    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            mappedBy = "user"
    )
    private Settings settings;

    @Column(name = "verified")
    private boolean verified;

    // Need a no-arg constructor if we specify a constructor with arguments (see 3 lines further)
    public User() {
    }

    public User(String firstName, String lastName, String email, String password, Boolean verified) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.verified = verified;
    }
    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.verified = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        return this.firstName + " " + this.lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Service> getEmployeeServices() {
        return employeeServices;
    }

    public void setEmployeeServices(List<Service> employeeServices) {
        this.employeeServices = employeeServices;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @PrePersist
    public void beforeInsert() {
        this.verified = false;
        this.setSettings(new Settings(false, false, this));
    }
}
