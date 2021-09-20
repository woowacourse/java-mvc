package nextstep.mvc.exception;

public class NoSuchHandlerAdapterException extends RuntimeException {

    public NoSuchHandlerAdapterException() {
    }

    public NoSuchHandlerAdapterException(String message) {
        super(message);
    }

    public NoSuchHandlerAdapterException(String message, Throwable cause) {
        super(message, cause);
    }
}
