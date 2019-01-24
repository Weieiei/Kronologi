package appointmentscheduler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ClientAppointmentConflictException extends RuntimeException {

    public ClientAppointmentConflictException() {
        super();
    }

    public ClientAppointmentConflictException(String message) {
        super(message);
    }

    public ClientAppointmentConflictException(String message, Throwable cause) {
        super(message, cause);
    }

}
