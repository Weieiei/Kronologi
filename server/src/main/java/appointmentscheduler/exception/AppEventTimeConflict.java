package appointmentscheduler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AppEventTimeConflict  extends RuntimeException{
    public AppEventTimeConflict() {
        super();
    }

    public AppEventTimeConflict(String message) {
        super(message);
    }

    public AppEventTimeConflict(String message, Throwable cause) {
        super(message, cause);
    }
}
