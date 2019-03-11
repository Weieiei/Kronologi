package appointmentscheduler.repository;

import appointmentscheduler.entity.appointment.CancelledAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CancelledRepository extends JpaRepository<CancelledAppointment, Long> {

    CancelledAppointment findByIdAndBusinessId(long appointmentId, long businessId);
}
