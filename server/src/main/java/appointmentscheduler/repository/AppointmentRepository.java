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

    Optional<Appointment> findByIdAndClientId(long appointmentId, long clientId);

    List<Appointment> findByClientId(long clientId);

    List<Appointment> findByClientIdAndStatus(long clientId, AppointmentStatus status);

    List<Appointment> findByDateAndClientIdAndStatus(LocalDate date, long clientId, AppointmentStatus status);

    List<Appointment> findByEmployeeId(long employeeId);

    List<Appointment> findByBusinessIdAndEmployeeId(long businessId, long employeeId);

    List<Appointment> findByDateAndEmployeeIdAndStatus(LocalDate date, long employeeId, AppointmentStatus status);

    List<Appointment> findByDateAndStatus(LocalDate date, AppointmentStatus status);

}
