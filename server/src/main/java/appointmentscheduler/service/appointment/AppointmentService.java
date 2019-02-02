package appointmentscheduler.service.appointment;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.appointment.AppointmentStatus;
import appointmentscheduler.entity.room.Room;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.exception.*;
import appointmentscheduler.repository.AppointmentRepository;
import appointmentscheduler.repository.EmployeeRepository;
import appointmentscheduler.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@org.springframework.stereotype.Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ShiftRepository shiftRepository;

    @Autowired
    public AppointmentService(
            AppointmentRepository appointmentRepository, EmployeeRepository employeeRepository, ShiftRepository shiftRepository
    ) {
        this.appointmentRepository = appointmentRepository;
        this.employeeRepository = employeeRepository;
        this.shiftRepository = shiftRepository;
    }

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    public Appointment findById(long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Appointment with id %d not found.", id)));
    }

    public List<Appointment> findByClientId(long id){
        return appointmentRepository.findByClientId(id);
    }

    public Appointment add(Appointment appointment) {
        // todo should this return a boolean instead ?
        // Right now this is void return type because it will throw exceptions if it doesn't work.
        // It will never reach the return statement if it fails any of the checks.
        // If this returns a boolean, then what do I return if the boolean is false?
        validate(appointment);

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

    /**
     * Checks to see if an appointment can be added. Any of the exceptions can be thrown if validation fails.
     * @param appointment The appointment to validate.
     * @throws ModelValidationException If the client and employee are the same person.
     * @throws EmployeeDoesNotOfferServiceException If the employee is not assigned to the service specified.
     * @throws EmployeeNotWorkingException If the employee does not have a shift on the date specified.
     * @throws EmployeeAppointmentConflictException If the employee is already booked on the date and time specified.
     * @throws ClientAppointmentConflictException If the client is already booked on the date and time specified.
     * @throws NoRoomAvailableException If there are no rooms available to perform the service specified.
     */
    private void validate(Appointment appointment) throws ModelValidationException, EmployeeDoesNotOfferServiceException, EmployeeNotWorkingException, EmployeeAppointmentConflictException, ClientAppointmentConflictException, NoRoomAvailableException {
        final Employee employee = appointment.getEmployee();

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

    }

    public Appointment findMyAppointmentById(long userId, long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);

        if (appointment == null || appointment.getClient().getId() != userId) {
            throw new NotYourAppointmentException("This appointment either belongs to another user or doesn't exist.");
        }

        return appointment;
    }

    public List<Employee> getAvailableEmployees(LocalDate date) {
        return employeeRepository.findByShifts_Date(date);
    }

    public Shift getEmployeesShift(long employeeId, LocalDate date) {
        return shiftRepository.findByEmployeeIdAndDate(employeeId, date).orElse(null);
    }
}
