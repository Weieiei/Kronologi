package appointmentscheduler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidUpdateException extends RuntimeException {

    public InvalidUpdateException() {
        super();
    }

    public InvalidUpdateException(String message) {
        super(message);
    }

    public InvalidUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

}
