package appointmentscheduler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PasswordNotProvidedExcetion extends RuntimeException {

    public PasswordNotProvidedExcetion() {
        super();
    }

    public PasswordNotProvidedExcetion(String message) {
        super(message);
    }

    public PasswordNotProvidedExcetion(String message, Throwable cause) {
        super(message, cause);
    }

}
