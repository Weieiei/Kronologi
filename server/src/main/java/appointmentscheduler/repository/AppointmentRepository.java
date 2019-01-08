package appointmentscheduler.repository;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.appointment.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByClientId(long clientId);
    List<Appointment> findByClientIdAndStatus(long clientId, AppointmentStatus status);

    List<Appointment> findByEmployeeId(long employeeId);

}
