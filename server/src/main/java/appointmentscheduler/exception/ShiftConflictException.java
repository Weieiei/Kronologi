package appointmentscheduler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ShiftConflictException extends RuntimeException {

    public ShiftConflictException() {
        super();
    }

    public ShiftConflictException(String message) {
        super(message);
    }

    public ShiftConflictException(String message, Throwable cause) {
        super(message, cause);
    }

}
