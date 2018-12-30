package appointmentscheduler.entity.user;

import appointmentscheduler.entity.Timestamps;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.service.Service;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends Timestamps {

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

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client")
    private List<Appointment> clientAppointments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    private List<Appointment> employeeAppointments;

    @JoinTable(
            name = "employee_services",
            joinColumns = { @JoinColumn(name = "employee_id") },
            inverseJoinColumns = { @JoinColumn(name = "service_id") }
    )
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Service> employeeServices;

    // Need a no-arg constructor if we specify a constructor with arguments (see 3 lines further)
    public User() { }

    public User(String firstName, String lastName, String email, String username, String password, UserType userType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.userType = userType;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public List<Appointment> getClientAppointments() {
        return clientAppointments;
    }

    public void setClientAppointments(List<Appointment> clientAppointments) {
        this.clientAppointments = clientAppointments;
    }

    public List<Appointment> getEmployeeAppointments() {
        return employeeAppointments;
    }

    public void setEmployeeAppointments(List<Appointment> employeeAppointments) {
        this.employeeAppointments = employeeAppointments;
    }

    public List<Service> getEmployeeServices() {
        return employeeServices;
    }

    public void setEmployeeServices(List<Service> employeeServices) {
        this.employeeServices = employeeServices;
    }

}
