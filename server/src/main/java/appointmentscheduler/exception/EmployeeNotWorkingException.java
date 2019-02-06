package appointmentscheduler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmployeeNotWorkingException extends RuntimeException {

    public EmployeeNotWorkingException() {
        super();
    }

    public EmployeeNotWorkingException(String message) {
        super(message);
    }

    public EmployeeNotWorkingException(String message, Throwable cause) {
        super(message, cause);
    }

}
