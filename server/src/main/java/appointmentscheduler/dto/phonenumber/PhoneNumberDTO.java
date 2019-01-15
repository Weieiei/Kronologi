package appointmentscheduler.dto.phonenumber;

public class PhoneNumberDTO {

    private String areaCode;
    private String number;

    public PhoneNumberDTO() { }

    public PhoneNumberDTO(String areaCode, String number) {
        this.areaCode = areaCode;
        this.number = number;
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

}
