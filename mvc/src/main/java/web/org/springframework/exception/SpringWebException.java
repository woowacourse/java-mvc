package web.org.springframework.exception;

public abstract class SpringWebException extends RuntimeException {

    protected SpringWebException(final String message) {
        super(message);
    }
}
