package appointmentscheduler.dto.service;

public class ServiceCreateDTO {
  //  private long serviceId;
    private String name;
    private int duration;
    private double price;

  /*  public long getId() {
        return serviceId;
    }

    public void setId(long serviceId) {
        this.serviceId = serviceId;
    }
*/
    public double getPrice() { return price;}

    public void setPrice(double price){this.price = price;}

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
