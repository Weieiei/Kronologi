package appointmentscheduler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmployeeDoesNotOfferServiceException extends RuntimeException {

    public EmployeeDoesNotOfferServiceException() {
        super();
    }

    public EmployeeDoesNotOfferServiceException(String message) {
        super(message);
    }

    public EmployeeDoesNotOfferServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
