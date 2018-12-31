package appointmentscheduler.service.appointment;

import appointmentscheduler.entity.appointment.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface IAppointmentService {

    Page<Appointment> findAll(Pageable pageable);

    Appointment findById(long id);

    Appointment add(Appointment appointment);

    Appointment update(long id, Appointment appointment);

    ResponseEntity<?> cancel(long id);

    ResponseEntity<?> delete(long id);

}
