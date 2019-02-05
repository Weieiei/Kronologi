package appointmentscheduler.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserNotAllowedToReviewServiceException extends RuntimeException{

    public UserNotAllowedToReviewServiceException() {
        super();
    }

    public UserNotAllowedToReviewServiceException(String message) {
        super(message);
    }

    public UserNotAllowedToReviewServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
