package appointmentscheduler.entity.role;

public enum  RoleEnum {

    ADMIN("ADMIN"),
    CLIENT("CLIENT"),
    EMPLOYEE("EMPLOYEE"),
    GUEST("GUEST");

    private String role;


    RoleEnum(final String role){
        this.role = role;
    }
    public String toString() {
        return this.role;
    }
}
