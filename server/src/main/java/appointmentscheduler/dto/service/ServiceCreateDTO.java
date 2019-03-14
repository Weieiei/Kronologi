package appointmentscheduler.dto.service;

public class ServiceCreateDTO {
  //  private long serviceId;
    private String name;
    private int duration;

  /*  public long getId() {
        return serviceId;
    }

    public void setId(long serviceId) {
        this.serviceId = serviceId;
    }
*/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
