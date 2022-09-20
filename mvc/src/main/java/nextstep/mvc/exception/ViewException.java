package nextstep.mvc.exception;

public class ViewException extends RuntimeException {

    public ViewException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
