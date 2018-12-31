package appointmentscheduler.repository;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.appointment.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Page<Appointment> findByClientId(long clientId, Pageable pageable);
    Page<Appointment> findByClientIdAndStatus(long clientId, AppointmentStatus status, Pageable pageable);

}
