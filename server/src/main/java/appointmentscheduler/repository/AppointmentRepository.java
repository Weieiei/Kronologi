package appointmentscheduler.repository;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.appointment.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Optional<Appointment> findByIdAndBusinessIdAndClientId(long appointmentId, long businessId, long clientId);

    Optional<Appointment> findByIdAndBusinessId(long appointmentId, long businessId);

    Optional<Appointment> findById(Long aLong);

    Optional<List<Appointment>> findByBusinessIdAndEmployeeId(long businessId, long employeeId);

    List<Appointment> findByClientId(long clientId);

    List<Appointment> findByBusinessId(long businessId);

    List<Appointment> findByClientIdAndBusinessId(long clientId, long businessId);

    List<Appointment> findByClientIdAndStatus(long clientId, AppointmentStatus status);

    List<Appointment> findByDateAndClientIdAndBusinessIdAndStatus(LocalDate date, long clientId,long businessId, AppointmentStatus status);

    List<Appointment> findByDateAndGuestIdAndBusinessIdAndStatus(LocalDate date, long guestId,long businessId, AppointmentStatus status);

    List<Appointment> findByEmployeeId(long employeeId);

    List<Appointment> findByDateAndEmployeeIdAndBusinessIdAndStatus(LocalDate date, long employeeId, long businessId, AppointmentStatus status);

    List<Appointment> findByEmployeeIdAndBusinessId(long employeeId, long businessId);

    List<Appointment> findByDateAndStatusAndBusinessId(LocalDate date, AppointmentStatus status, long businessId);
}
