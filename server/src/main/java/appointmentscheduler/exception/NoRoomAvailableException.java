package appointmentscheduler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class NoRoomAvailableException extends RuntimeException {

    public NoRoomAvailableException() {
        super();
    }

    public NoRoomAvailableException(String message) {
        super(message);
    }

    public NoRoomAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

}
