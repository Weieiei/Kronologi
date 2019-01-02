package appointmentscheduler.service.appointment;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.appointment.AppointmentStatus;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService implements IAppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    @Override
    public Appointment findById(long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Appointment with id %d not found.", id)));
    }

    @Override
    public Appointment add(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment update(long id, Appointment appointment) {

        return appointmentRepository.findById(id).map(a -> {

            a.setClient(appointment.getClient());
            a.setEmployee(appointment.getEmployee());
            a.setService(appointment.getService());
            a.setStartTime(appointment.getStartTime());
            a.setEndTime(appointment.getEndTime());
            a.setNotes(appointment.getNotes());

            return appointmentRepository.save(a);

        }).orElseThrow(() -> new ResourceNotFoundException(String.format("Appointment with id %d not found.", id)));

    }

    @Override
    public ResponseEntity<?> cancel(long id) {

        return appointmentRepository.findById(id).map(a -> {

            a.setStatus(AppointmentStatus.cancelled);
            appointmentRepository.save(a);

            return ResponseEntity.ok().build();

        }).orElseThrow(() -> new ResourceNotFoundException(String.format("Appointment with id %d not found.", id)));

    }

    @Override
    public ResponseEntity<?> delete(long id) {

        return appointmentRepository.findById(id).map(a -> {

            appointmentRepository.delete(a);
            return ResponseEntity.ok().build();

        }).orElseThrow(() -> new ResourceNotFoundException(String.format("Appointment with id %d not found.", id)));

    }
}
