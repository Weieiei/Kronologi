package appointmentscheduler.dto.appointment;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentDTO {

    private long businessId;
    private long employeeId;
    private long serviceId;

    private LocalDate date;
    private LocalTime startTime;

    private String notes;

    public long getBusinessId() { return businessId;}

    public void setBusinessId(long businessId){this.businessId = businessId;}
    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Appointment convertToAppointment(User client, Employee employee, Service service, Business business){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<AppointmentDTO, Appointment>() {
            protected void configure() {
                skip().setId(0);
            }
        });
        Appointment appointment = modelMapper.map(this, Appointment.class);
        appointment.setClient(client);
        appointment.setEmployee(employee);
        appointment.setService(service);
        appointment.setBusiness(business);
        //set endtime with service duration
        appointment.setEndTime(startTime.plusMinutes(service.getDuration()));

        return appointment;
    }
}
