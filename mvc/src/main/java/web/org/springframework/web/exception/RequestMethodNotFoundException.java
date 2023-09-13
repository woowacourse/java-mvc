package web.org.springframework.web.exception;

public class RequestMethodNotFoundException extends RuntimeException {

    public RequestMethodNotFoundException(String message) {
        super(message);
    }
}
