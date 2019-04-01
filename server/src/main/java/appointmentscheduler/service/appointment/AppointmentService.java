package appointmentscheduler.service.appointment;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.appointment.AppointmentStatus;
import appointmentscheduler.entity.appointment.CancelledAppointment;
import appointmentscheduler.entity.appointment.GeneralAppointment;
import appointmentscheduler.entity.employee_service.EmployeeService;
import appointmentscheduler.entity.googleEntity.SyncEntity;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.entity.verification.GoogleCred;
import appointmentscheduler.exception.*;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.repository.*;
import appointmentscheduler.service.googleService.GoogleSyncService;
import appointmentscheduler.service.googleService.JPADataStoreFactory;
import appointmentscheduler.service.googleService.JPADataStoreService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import appointmentscheduler.util.DateConflictChecker;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import appointmentscheduler.repository.AppointmentRepository;
import appointmentscheduler.repository.CancelledRepository;
import appointmentscheduler.repository.EmployeeRepository;
import appointmentscheduler.repository.ShiftRepository;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@org.springframework.stereotype.Service
public class AppointmentService {




    GoogleClientSecrets clientSecrets;

    @Value("${google.client.client-id}")
    private String clientId;
    @Value("${google.client.client-secret}")
    private String clientSecret;
    @Value("${google.client.redirectUri}")
    private String redirectURI;



    private static final String APPLICATION_NAME = "";
    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static com.google.api.services.calendar.Calendar client;


    private GoogleSyncService googleSyncService;
    private CancelledRepository cancelledRepository;
    private AppointmentRepository appointmentRepository;
    private EmployeeRepository employeeRepository;
    private ShiftRepository shiftRepository;
    private GeneralAppointmentRepository generalAppointmentRepository;
    private GoogleCredentialRepository googleCredentialRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> findByBusinessId(long id) {
        return appointmentRepository.findByBusinessId(id);
    }

    public List<Appointment> findByEmployeeIdAndBusinessId(long employeeId, long businessId) {
        return appointmentRepository.findByEmployeeIdAndBusinessId(employeeId, businessId);
    }

    public List<Appointment> findByBusinessIdAndEmployeeId(long businessId, long employeeId) {
        return appointmentRepository.findByBusinessIdAndEmployeeId(businessId, employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Appointment list with employee id %d " +
                        "and business id %d not found", employeeId, businessId)));
    }

    @Autowired
    public AppointmentService(
            AppointmentRepository appointmentRepository, EmployeeRepository employeeRepository, ShiftRepository shiftRepository, CancelledRepository cancelledRepository,
            GeneralAppointmentRepository generalAppointmentRepository, GoogleCredentialRepository googleCredentialRepository, GoogleSyncService googleSyncService
    ) {
        this.googleCredentialRepository = googleCredentialRepository;
        this.cancelledRepository = cancelledRepository;
        this.appointmentRepository = appointmentRepository;
        this.employeeRepository = employeeRepository;
        this.shiftRepository = shiftRepository;
        this.generalAppointmentRepository = generalAppointmentRepository;
        this.googleCredentialRepository = googleCredentialRepository;
        this.googleSyncService = googleSyncService;
    }

    public List<Appointment> findByClientIdAndBusinessId(long clientId, long businessId){
        Optional<List<Appointment>> opt =
                Optional.ofNullable(appointmentRepository.findByClientIdAndBusinessId(clientId, businessId));
        return opt.orElseThrow(() -> new ResourceNotFoundException(String.format("Appointment with client id %d " +
                "and business id %d not found", clientId, businessId)));
    }

/*
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


        //Save to google calendar of employee and / or client if they have their credentials in our db
        if(googleCredentialRepository.findByKey(String.valueOf(appointment.getEmployee().getId())).isPresent()) {
            saveEventToGoogleCalendar(appointment, appointment.getEmployee());
        }
        if(googleCredentialRepository.findByKey(String.valueOf(appointment.getClient().getId())).isPresent()) {
            saveEventToGoogleCalendar(appointment, appointment.getClient());
        }

        return appointmentRepository.save(appointment);
    }

    public Appointment update(long appointmentId, long clientId, Appointment appointment) {

        return appointmentRepository.findByIdAndBusinessIdAndClientId(appointmentId, appointment.getBusiness().getId(), clientId).map(a -> {

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

    public Appointment cancel(long appointmentId, long businessId, long clientId) {

        Appointment appointment = appointmentRepository.findByIdAndBusinessIdAndClientId(appointmentId, businessId , clientId)
                .orElseThrow(() -> new NotYourAppointmentException("This appointment either belongs to another user or doesn't exist."));

        if (appointment.getStatus().equals(AppointmentStatus.CANCELLED)) {
            throw new AppointmentAlreadyCancelledException("This appointment is already cancelled.");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);

        return appointmentRepository.save(appointment);

    }

    public void  googleCalendarEvents(Events events, User user){
        List<GeneralAppointment> generalAppointmentList = new ArrayList<>();


        for(Event event : events.getItems()){

            Employee employee = employeeRepository.findById(user.getId()).get();
            GeneralAppointment generalAppointment = new GeneralAppointment();
            Date startDate= new Date(event.getStart().getDateTime().getValue());
            Date endDate = new Date(event.getEnd().getDateTime().getValue());
            Instant dateInstant = startDate.toInstant();
            Instant endDateInstant = endDate.toInstant();
            ZonedDateTime  zdt = dateInstant.atZone(ZoneId.systemDefault());
            ZonedDateTime endZoneDateTime =  endDateInstant.atZone(ZoneId.systemDefault());
            LocalDateTime finalStartDate = zdt.toLocalDateTime();
            LocalDateTime finalEndDate = endZoneDateTime.toLocalDateTime();

            generalAppointment.setDate(finalStartDate.toLocalDate());
            generalAppointment.setStartTime(finalStartDate.toLocalTime());
            generalAppointment.setEndTime(finalEndDate.toLocalTime());
            generalAppointment.setEmployee(employee);
            generalAppointment.setBusiness(employee.getBusiness());

            generalAppointmentList.add(generalAppointment);
        }

        //keep track of sync token
        SyncEntity syncEntity = new SyncEntity(events.getNextSyncToken(), user);
        googleSyncService.saveSyncToken(syncEntity);

        generalAppointmentRepository.saveAll(generalAppointmentList);
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

        GoogleCred employeeCred =  googleCredentialRepository.findByKey(String.valueOf(employee.getId())).get();

        if(googleCredentialRepository.findByKey(String.valueOf(employee.getId())).isPresent() && googleSyncService.findById(employee.getId()) != null){
            checkCalendarSync(googleCredentialRepository.findByKey(String.valueOf(employee.getId())).get().getAccessToken(), googleSyncService.findById(employee.getId()).getSyncToken(), employee);
        }

        // Make sure the client and employee are not the same
        if (appointment.getClient().equals(employee)) {
            throw new ModelValidationException("You cannot book an appointment with yourself.");
        }

        // Make sure the employee can perform the service requested
        boolean employeeCanDoService = false;
        for (EmployeeService service : appointment.getService().getEmployees()) {
            if(service.getEmployee().getId() == employee.getId()){
                employeeCanDoService = true;
                break;
            }
        }


        if (!employeeCanDoService) {
            throw new EmployeeDoesNotOfferServiceException("The employee does not perform that service.");
        }

        // Check if the employee is working on the date specified
        boolean employeeIsWorking = employee.isWorking(appointment.getDate(), appointment.getStartTime(), appointment.getEndTime());

        if (!employeeIsWorking) {
            throw new EmployeeNotWorkingException("The employee does not have a shift.");
        }

        // Check if the employee does not have an appointment scheduled already in that time slot
        List<Appointment> employeeAppointments = appointmentRepository.findByDateAndEmployeeIdAndBusinessIdAndStatus(appointment.getDate(), employee.getId(), appointment.getBusiness().getId(), AppointmentStatus.CONFIRMED);
        if (DateConflictChecker.hasConflictList(employeeAppointments, appointment, modifying)) {
            throw new EmployeeAppointmentConflictException("There is a conflicting appointment already booked with that employee.");
        }

        // Check if the client does not have an appointment scheduled already
        List<Appointment> clientAppointments = appointmentRepository.findByDateAndClientIdAndBusinessIdAndStatus(appointment.getDate(), appointment.getClient().getId(),appointment.getBusiness().getId(), AppointmentStatus.CONFIRMED);
        if(DateConflictChecker.hasConflictList(clientAppointments, appointment, modifying)) {
            throw new ClientAppointmentConflictException("You already have another appointment booked at the same time.");
        }
    }

    public Appointment findMyAppointmentById(long userId, long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);

        if (appointment == null || appointment.getClient().getId() != userId) {
            throw new NotYourAppointmentException("This appointment either belongs to another user or doesn't exist.");
        }

        return appointment;
    }

    public Appointment findMyAppointmentByIdAndBusinessId(long userId, long appointmentId, long businessId) {
        Appointment appointment = appointmentRepository.findByIdAndBusinessId(appointmentId, businessId).orElse(null);

        if (appointment == null || appointment.getClient().getId() != userId) {
            throw new NotYourAppointmentException("This appointment either belongs to another user or doesn't exist.");
        }

        return appointment;
    }


    public List<Employee> getAvailableEmployeesByServiceAndByDate(long serviceId, LocalDate date) {
        return employeeRepository.findByServices_IdAndShifts_Date(serviceId, date);
    }

    public List<Shift> getEmployeeShiftsByDateAndBusinessId(LocalDate date, long businessId) {
        return shiftRepository.findByDateAndBusinessId(date, businessId);
    }

    public List<Appointment> getConfirmedAppointmentsByDateAndBusinessId(LocalDate date, long businessId) {
        return appointmentRepository.findByDateAndStatusAndBusinessId(date, AppointmentStatus.CONFIRMED, businessId);
    }

    public Map<String, String> cancel(CancelledAppointment cancel) {
        return appointmentRepository.findById(cancel.getAppointment().getId()).map(a -> {

            a.setStatus(AppointmentStatus.CANCELLED);
            appointmentRepository.save(a);
            cancelledRepository.save(cancel);

            return message("Appointment was successfully  cancelled!");

        }).orElseThrow(() -> new ResourceNotFoundException(String.format("Appointment with id %d not found.", cancel.getAppointment().getId())));

    }

    public Map<String, String> cancel(CancelledAppointment cancel, long businessId) {
        return appointmentRepository.findByIdAndBusinessId(cancel.getAppointment().getId(), businessId).map(a -> {

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

    public CancelledAppointment findByCancelledIdAndBusinessId(long id, long businessId) {
        return cancelledRepository.findByIdAndBusinessId(id, businessId);
    }


    private Map<String, String> message(String message) {
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        return map;
    }

    /**
     * Syncs with Google Calendar using stored Sync token.
     * Refer to : https://developers.google.com/calendar/v3/sync
     *
     * @param accessToken user acceess token have access to the googel calendar
     * @param syncToken token to identify last element app sync'd with
     * @param employee id of employee to sync to find  possible new conflicts
     */
   private void checkCalendarSync(String accessToken, String syncToken, Employee employee) {
       try {
           httpTransport = GoogleNetHttpTransport.newTrustedTransport();
           GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);

           client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                   .setApplicationName(APPLICATION_NAME).build();

           com.google.api.services.calendar.Calendar.Events.List request = client.events().list("primary");
           request.setSyncToken(syncToken);

           Events eventList = request.execute();

           if (eventList.size() > 0) {
               this.googleCalendarEvents(eventList,employee);
           }
       } catch (Exception e) {

       }
   }


   private void saveEventToGoogleCalendar(Appointment appointment, User userToSave){
       try{
           GoogleCred googleCred = googleCredentialRepository.findByKey(String.valueOf(userToSave.getId())).get();

           httpTransport = GoogleNetHttpTransport.newTrustedTransport();
           GoogleCredential credential = new GoogleCredential().setAccessToken(googleCred.getAccessToken());

           client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                   .setApplicationName(APPLICATION_NAME).build();


           Event event = new Event();

           LocalDateTime startTime = LocalDateTime.of(appointment.getDate(), appointment.getStartTime());
           Date finalStartTime = Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant());
           DateTime startDateTime = new DateTime(finalStartTime, Calendar.getInstance().getTimeZone());


           LocalDateTime endTime = LocalDateTime.of(appointment.getDate(), appointment.getEndTime());
           Date finalEndTime = Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant());
           DateTime dt2 = new DateTime(finalEndTime, Calendar.getInstance().getTimeZone());

           event.setStart(new EventDateTime().setDateTime(startDateTime));
           event.setEnd(new EventDateTime().setDateTime(dt2));
           event.setId(UUID.randomUUID().toString().replaceAll("-",""));
           event.setSummary(appointment.getService().getName() + " with " + appointment.getEmployee().getFirstName() + " on " + appointment.getDate().toString());
           Event.Creator creator = new Event.Creator().setEmail(appointment.getClient().getEmail()).setDisplayName(appointment.getClient().getFirstName() + " "  + appointment.getClient().getLastName());
           event.setCreator(creator);

           client.events().insert("primary", event).execute();
       }catch( Exception e){
           e.printStackTrace();
       }

   }

}
