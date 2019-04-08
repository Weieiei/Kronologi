package appointmentscheduler.dto.user;

import appointmentscheduler.dto.phonenumber.PhoneNumberDTO;

public class GuestDTO {

    private String firstName;
    private String lastName;
    private PhoneNumberDTO phoneNumber;
    private String email;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public PhoneNumberDTO getPhoneNumber(){
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumberDTO phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}