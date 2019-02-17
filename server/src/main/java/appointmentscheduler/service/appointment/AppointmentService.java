package appointmentscheduler.service.appointment;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.appointment.AppointmentStatus;
import appointmentscheduler.entity.appointment.CancelledAppointment;
import appointmentscheduler.entity.room.Room;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.exception.*;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.repository.AppointmentRepository;
import appointmentscheduler.repository.EmployeeRepository;
import appointmentscheduler.repository.ShiftRepository;
import appointmentscheduler.repository.CancelledRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;

@org.springframework.stereotype.Service
public class AppointmentService {

    private final CancelledRepository cancelledRepository;
    private final AppointmentRepository appointmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ShiftRepository shiftRepository;


    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> findByEmployeeId(long employeeId) {
        return appointmentRepository.findByEmployeeId(employeeId);
    }

    @Autowired
    public AppointmentService(
            AppointmentRepository appointmentRepository, EmployeeRepository employeeRepository, ShiftRepository shiftRepository, CancelledRepository cancelledRepository
    ) {
        this.cancelledRepository = cancelledRepository;
        this.appointmentRepository = appointmentRepository;
        this.employeeRepository = employeeRepository;
        this.shiftRepository = shiftRepository;
    }

    public List<Appointment> findByClientId(long id) {
        return appointmentRepository.findByClientId(id);
    }
/* //from Ema's branch
    //for admin to view all appointments for a client
    public List<Appointment> findByClientId(long id){
        Optional<List<Appointment>> opt = Optional.ofNullable(appointmentRepository.findByClientId(id));
        return opt.orElseThrow(() -> new ResourceNotFoundException(String.format("Appointment with client id %d not found", id)));
    }


    //for admin to see employee's appointments
    public List<Appointment> findByEmployeeId(long id) {
        Optional<List<Appointment>> opt = Optional.ofNullable(appointmentRepository.findByEmployeeId(id));
        return opt.orElseThrow(() -> new ResourceNotFoundException(String.format("Appointment with employee id %d not found.", id)));
    }
*/
    public Appointment add(Appointment appointment) {
        // todo should this return a boolean instead ?
        // Right now this is void return type because it will throw exceptions if it doesn't work.
        // It will never reach the return statement if it fails any of the checks.
        // If this returns a boolean, then what do I return if the boolean is false?

        // Throwing exception is nice because then you can display the http error message to the user, indicating where
        // the validation went wrong
        validate(appointment, false);

        return appointmentRepository.save(appointment);
    }

    public Appointment update(long appointmentId, long clientId, Appointment appointment) {

        return appointmentRepository.findByIdAndClientId(appointmentId, clientId).map(a -> {

            a.setClient(appointment.getClient());
            a.setEmployee(appointment.getEmployee());
            a.setService(appointment.getService());
            a.setDate(appointment.getDate());
            a.setStartTime(appointment.getStartTime());
            a.setEndTime(appointment.getEndTime());
            a.setNotes(appointment.getNotes());

            validate(a, true);

            return appointmentRepository.save(a);

        }).orElseThrow(() -> new ResourceNotFoundException("This appointment either belongs to another user or doesn't exist."));

    }

    public Appointment cancel(long appointmentId, long clientId) {

        Appointment appointment = appointmentRepository.findByIdAndClientId(appointmentId, clientId)
                .orElseThrow(() -> new NotYourAppointmentException("This appointment either belongs to another user or doesn't exist."));

        if (appointment.getStatus().equals(AppointmentStatus.CANCELLED)) {
            throw new AppointmentAlreadyCancelledException("This appointment is already cancelled.");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);

        return appointmentRepository.save(appointment);

    }

    /**
     * Checks to see if an appointment can be added. Any of the exceptions can be thrown if validation fails.
     *
     * @param appointment The appointment to validate.
     * @param modifying   Whether or not it's an appointment being modified.
     * @throws ModelValidationException             If the client and employee are the same person.
     * @throws EmployeeDoesNotOfferServiceException If the employee is not assigned to the service specified.
     * @throws EmployeeNotWorkingException          If the employee does not have a shift on the date specified.
     * @throws EmployeeAppointmentConflictException If the employee is already booked on the date and time specified.
     * @throws ClientAppointmentConflictException   If the client is already booked on the date and time specified.
     * @throws NoRoomAvailableException             If there are no rooms available to perform the service specified.
     */
    private void validate(Appointment appointment, boolean modifying) throws ModelValidationException, EmployeeDoesNotOfferServiceException, EmployeeNotWorkingException, EmployeeAppointmentConflictException, ClientAppointmentConflictException, NoRoomAvailableException {
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
        List<Appointment> employeeAppointments = appointmentRepository.findByDateAndEmployeeIdAndStatus(appointment.getDate(), employee.getId(), AppointmentStatus.CONFIRMED);

        for (Appointment employeeAppointment : employeeAppointments) {
            if (employeeAppointment.isConflicting(appointment) && !(modifying && employeeAppointment.equals(appointment))) {
                throw new EmployeeAppointmentConflictException("There is a conflicting appointment already booked with that employee.");
            }
        }

        // Check if the client does not have an appointment scheduled already
        List<Appointment> clientAppointments = appointmentRepository.findByDateAndClientIdAndStatus(appointment.getDate(), appointment.getClient().getId(), AppointmentStatus.CONFIRMED);

        for (Appointment clientAppointment : clientAppointments) {
            if (clientAppointment.isConflicting(appointment) && !(modifying && clientAppointment.equals(appointment))) {
                throw new ClientAppointmentConflictException("You already have another appointment booked at the same time.");
            }
        }

        // Check if there is an available room for the employee to work in
        List<Appointment> allAppointmentsOnDate = appointmentRepository.findByDateAndStatus(appointment.getDate(), AppointmentStatus.CONFIRMED);

        Set<Room> roomSet = new HashSet<>(appointment.getService().getRooms());
        for (Appointment a : allAppointmentsOnDate) {
            if (a.isConflicting(appointment) && !roomSet.isEmpty() && !(modifying && a.equals(appointment))) {
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

    public List<Employee> getAvailableEmployeesByServiceAndByDate(long serviceId, LocalDate date) {
        return employeeRepository.findByServices_IdAndShifts_Date(serviceId, date);
    }

    public List<Shift> getEmployeeShiftsByDate(LocalDate date) {
        return shiftRepository.findByDate(date);
    }

    public List<Appointment> getConfirmedAppointmentsByDate(LocalDate date) {
        return appointmentRepository.findByDateAndStatus(date, AppointmentStatus.CONFIRMED);
    }

    public Map<String, String> cancel(CancelledAppointment cancel) {
        return appointmentRepository.findById(cancel.getAppointment().getId()).map(a -> {

            a.setStatus(AppointmentStatus.CANCELLED);
            appointmentRepository.save(a);
            cancelledRepository.save(cancel);

            return message("Appointment was successfully  cancelled!");

        }).orElseThrow(() -> new ResourceNotFoundException(String.format("Appointment with id %d not found.", cancel.getAppointment().getId())));

    }

    public ResponseEntity<?> delete(long id) {

        return appointmentRepository.findById(id).map(a -> {

            appointmentRepository.delete(a);
            return ResponseEntity.ok().build();

        }).orElseThrow(() -> new ResourceNotFoundException(String.format("Appointment with id %d not found.", id)));
    }

    public CancelledAppointment findByCancelledId(long id) {
        return cancelledRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Appointment with id %d not found.", id)));
    }


    private Map<String, String> message(String message) {
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        return map;
    }
}
