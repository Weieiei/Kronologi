package appointmentscheduler.service.appointment;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.appointment.AppointmentStatus;
import appointmentscheduler.entity.room.Room;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.exception.*;
import appointmentscheduler.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    public Appointment findById(long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Appointment with id %d not found.", id)));
    }

    public List<Appointment> findByClientId(long id) {
        return appointmentRepository.findByClientId(id);
    }

    public Appointment add(Appointment appointment) {
        Employee employee = appointment.getEmployee();

        // Make sure the client and employee are not the same
        if (appointment.getClient().equals(employee)) {
            throw new ModelValidationException("You cannot book an appointment with yourself.");
        }

        // Make sure the employee can perform the service requested
        boolean employeeCanDoService = appointment.getService().getEmployees().contains(employee);

        if (!employeeCanDoService) {
            throw new EmployeeDoesNotOfferServiceException("The employee does not perform that service.");
        }

        // Check if the employee is working on the date specified
        boolean employeeIsWorking = employee.isWorking(appointment.getDate(), appointment.getStartTime(), appointment.getEndTime());

        if (!employeeIsWorking) {
            throw new EmployeeNotWorkingException("The employee does not have a shift.");
        }

        // Check if the employee does not have an appointment scheduled already in that time slot
        List<Appointment> employeeAppointments = appointmentRepository.findByDateAndEmployeeIdAndStatus(appointment.getDate(), employee.getId(), AppointmentStatus.confirmed);

        for (Appointment employeeAppointment : employeeAppointments) {
            if (employeeAppointment.isConflicting(appointment)) {
                throw new EmployeeAppointmentConflictException("There is a conflicting appointment already booked with that employee");
            }
        }

        // Check if the client does not have an appointment scheduled already
        List<Appointment> clientAppointments = appointmentRepository.findByDateAndClientIdAndStatus(appointment.getDate(), appointment.getClient().getId(), AppointmentStatus.confirmed);

        for (Appointment clientAppointment : clientAppointments) {
            if (clientAppointment.isConflicting(appointment)) {
                throw new ClientAppointmentConflictException("There is a conflicting appointment already booked with that client");
            }
        }

        // Check if there is an available room for the employee to work in
        List<Appointment> allAppointmentsOnDate = appointmentRepository.findByDateAndStatus(appointment.getDate(), AppointmentStatus.confirmed);

        Set<Room> roomSet = new HashSet<>(appointment.getService().getRooms());
        for (Appointment a : allAppointmentsOnDate) {
            if (a.isConflicting(appointment) && !roomSet.isEmpty()) {
                for (Room room : a.getService().getRooms()) {
                    roomSet.remove(room);
                }
            }
        }
        if (roomSet.isEmpty()) {
            throw new NoRoomAvailableException("There are no rooms available");
        }

        return appointmentRepository.save(appointment);
    }

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

    public ResponseEntity<?> cancel(long id) {

        return appointmentRepository.findById(id).map(a -> {

            a.setStatus(AppointmentStatus.cancelled);
            appointmentRepository.save(a);

            return ResponseEntity.ok().build();

        }).orElseThrow(() -> new ResourceNotFoundException(String.format("Appointment with id %d not found.", id)));

    }

    public ResponseEntity<?> delete(long id) {

        return appointmentRepository.findById(id).map(a -> {

            appointmentRepository.delete(a);
            return ResponseEntity.ok().build();

        }).orElseThrow(() -> new ResourceNotFoundException(String.format("Appointment with id %d not found.", id)));

    }
}
