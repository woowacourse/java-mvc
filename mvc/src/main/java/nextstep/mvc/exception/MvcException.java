package nextstep.mvc.exception;

public class MvcException extends RuntimeException {

    public MvcException(String message) {
        super(message);
    }

    public MvcException(String message, Throwable cause) {
        super(message, cause);
    }
}
