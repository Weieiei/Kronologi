package appointmentscheduler.entity.appointment;

import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentFactory {
    public static Appointment createAppointment(User client, Employee employee, Service service, LocalDate date, LocalTime startTime, String notes) {
        final Appointment appointment = new Appointment();

        appointment.setClient(client);
        appointment.setEmployee(employee);
        appointment.setService(service);
        appointment.setDate(date);
        appointment.setStartTime(startTime);
        appointment.setNotes(notes);

        return appointment;
    }

    public static Appointment createAppointment(Business business, User client, Employee employee, Service service,
                                                LocalDate date, LocalTime startTime, String notes) {
        final Appointment appointment = new Appointment();

        appointment.setBusiness(business);
        appointment.setClient(client);
        appointment.setEmployee(employee);
        appointment.setService(service);
        appointment.setDate(date);
        appointment.setStartTime(startTime);
        appointment.setNotes(notes);

        return appointment;
    }
}
