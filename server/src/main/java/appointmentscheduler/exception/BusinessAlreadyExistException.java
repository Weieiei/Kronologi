package appointmentscheduler.exception;

public class BusinessAlreadyExistException extends RuntimeException {
    public BusinessAlreadyExistException() {
        super();
    }

    public BusinessAlreadyExistException(String message) {
        super(message);
    }

    public BusinessAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
