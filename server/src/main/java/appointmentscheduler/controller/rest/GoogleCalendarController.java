package appointmentscheduler.controller.rest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import appointmentscheduler.controller.rest.AbstractController;
import appointmentscheduler.entity.verification.GoogleCred;
import appointmentscheduler.repository.GoogleCredentialRepository;
import appointmentscheduler.repository.ServiceRepository;
import appointmentscheduler.serializer.ObjectMapperFactory;
import appointmentscheduler.service.appointment.AppointmentService;
import appointmentscheduler.service.employee.EmployeeShiftService;
import appointmentscheduler.service.googleService.JPADataStoreFactory;
import appointmentscheduler.service.googleService.JPADataStoreService;
import appointmentscheduler.service.service.ServiceService;
import appointmentscheduler.service.user.UserService;
import appointmentscheduler.util.JwtProvider;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.DataStoreUtils;
import com.google.api.services.calendar.model.EventDateTime;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar.Events;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;

@Controller
@RequestMapping(value = "/external/google")
public class GoogleCalendarController extends AbstractController {

    private final static Log logger = LogFactory.getLog(GoogleCalendarController.class);
    private static final String APPLICATION_NAME = "";
    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static com.google.api.services.calendar.Calendar client;

    GoogleClientSecrets clientSecrets;
    GoogleAuthorizationCodeFlow flow;
    Credential credential;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    UserService userService;

    @Autowired
    GoogleCredentialRepository repo;



    @Value("${google.client.client-id}")
    private String clientId;
    @Value("${google.client.client-secret}")
    private String clientSecret;
    @Value("${google.client.redirectUri}")
    private String redirectURI;

    private Set<Event> events = new HashSet<>();

    final DateTime date1 = new DateTime(new Date());

    private JPADataStoreFactory dataStore;

    @Autowired
    public GoogleCalendarController(JPADataStoreFactory jpaDataStoreFactory) {
         dataStore  = jpaDataStoreFactory;
    }
    public void setEvents(Set<Event> events) {
        this.events = events;
    }



    @GetMapping(value = "/login/google")
    public ResponseEntity googleConnectionStatus(HttpServletRequest request) throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("url", authorize());

        return new ResponseEntity<>(obj.toString(), HttpStatus.OK);
        //return ResponseEntity.ok(authorize());
    }

    @GetMapping(value = "/login/calendarCallback")
    public RedirectView oauth2Callback(@RequestParam(value = "code") String code) {
        com.google.api.services.calendar.model.Events eventList;
        String message;
        try {

            TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();
            logger.warn(response);
            logger.warn(response.getAccessToken());
            credential = flow.createAndStoreCredential(response, String.valueOf(getSessionUser()));
            client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();

            JsonFactory jsonFactory = new JacksonFactory();
            GoogleCredential credential = new GoogleCredential.Builder()
                    .setTransport(httpTransport)
                    .setJsonFactory(jsonFactory)
                    .setClientSecrets("client_id", "client_secret").build();

            credential.setAccessToken("access_token");

            Events events = client.events();
            eventList = events.list("primary").setTimeMin(date1).execute();

            message = eventList.getItems().toString();
            appointmentService.googleCalendarEvents(eventList.getItems(), 5);
            System.out.println("My:" + eventList.getItems());
        } catch (Exception e) {
            logger.warn("Exception while handling OAuth2 callback (" + e.getMessage() + ")."
                    + " Redirecting to google connection status page.");
            message = "Exception while handling OAuth2 callback (" + e.getMessage() + ")."
                    + " Redirecting to google connection status page.";
        }

        System.out.println("cal message:" + message);
        return new RedirectView("http://localhost:4200/syncCalendars");
    }

    public Set<Event> getEvents() throws IOException {
        return this.events;
    }

    private String authorize() throws Exception {
        AuthorizationCodeRequestUrl authorizationUrl;
        if (flow == null) {
            Details web = new Details();
            web.setClientId(clientId);
            web.setClientSecret(clientSecret);
            clientSecrets = new GoogleClientSecrets().setWeb(web);
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
                    Collections.singleton(CalendarScopes.CALENDAR)).setAccessType("offline").setDataStoreFactory(dataStore).build();
        }

        JwtProvider jwt = new JwtProvider();

        String calendarToken = jwt.generateCalendarToken(userService.findUserById(getUserId()),SecurityContextHolder.getContext().getAuthentication() );

        authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectURI).setState(calendarToken);
        return authorizationUrl.build();
    }



}