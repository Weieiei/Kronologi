package appointmentscheduler.repository;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.appointment.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Optional<Appointment> findByIdAndBusinessIdAndClientId(long appointmentId, long businessId, long clientId);

    List<Appointment> findByClientId(long clientId);

    List<Appointment> findByClientIdAndStatus(long clientId, AppointmentStatus status);

    List<Appointment> findByDateAndClientIdAndBusinessIdAndStatus(LocalDate date, long clientId,long businessId, AppointmentStatus status);

    List<Appointment> findByEmployeeId(long employeeId);

    List<Appointment> findByDateAndEmployeeIdAndBusinessIdAndStatus(LocalDate date, long employeeId, long businessId, AppointmentStatus status);

    List<Appointment> findByDateAndStatusAndBusinessId(LocalDate date, AppointmentStatus status, long businessId);

}
