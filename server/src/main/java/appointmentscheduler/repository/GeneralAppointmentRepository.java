package appointmentscheduler.repository;

import appointmentscheduler.entity.appointment.GeneralAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralAppointmentRepository extends JpaRepository<GeneralAppointment, Long> {
}
