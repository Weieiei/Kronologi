package appointmentscheduler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmployeeAppointmentConflictException extends RuntimeException {

    public EmployeeAppointmentConflictException() {
        super();
    }

    public EmployeeAppointmentConflictException(String message) {
        super(message);
    }

    public EmployeeAppointmentConflictException(String message, Throwable cause) {
        super(message, cause);
    }

}
