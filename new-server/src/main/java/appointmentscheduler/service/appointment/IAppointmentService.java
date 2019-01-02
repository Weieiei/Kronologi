package appointmentscheduler.service.appointment;

import appointmentscheduler.entity.appointment.Appointment;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IAppointmentService {

    List<Appointment> findAll();

    Appointment findById(long id);

    Appointment add(Appointment appointment);

    Appointment update(long id, Appointment appointment);

    ResponseEntity<?> cancel(long id);

    ResponseEntity<?> delete(long id);

}
