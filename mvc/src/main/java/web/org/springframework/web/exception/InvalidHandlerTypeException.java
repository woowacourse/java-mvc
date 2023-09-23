package web.org.springframework.web.exception;

public class InvalidHandlerTypeException extends RuntimeException {
    public InvalidHandlerTypeException(String message) {
        super(message);
    }
}
