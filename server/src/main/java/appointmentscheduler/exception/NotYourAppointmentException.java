package appointmentscheduler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotYourAppointmentException extends RuntimeException {

    public NotYourAppointmentException() {
        super();
    }

    public NotYourAppointmentException(String message) {
        super(message);
    }

    public NotYourAppointmentException(String message, Throwable cause) {
        super(message, cause);
    }

}
