package appointmentscheduler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AppointmentAlreadyCancelledException extends RuntimeException {

    public AppointmentAlreadyCancelledException() {
        super();
    }

    public AppointmentAlreadyCancelledException(String message) {
        super(message);
    }

    public AppointmentAlreadyCancelledException(String message, Throwable cause) {
        super(message, cause);
    }

}
