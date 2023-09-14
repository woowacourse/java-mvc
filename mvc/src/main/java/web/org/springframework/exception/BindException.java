package web.org.springframework.exception;

public class BindException extends SpringWebException {

    public BindException(final String message) {
        super(message);
    }
}
