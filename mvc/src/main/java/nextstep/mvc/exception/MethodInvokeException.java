package nextstep.mvc.exception;

public class MethodInvokeException extends RuntimeException {

    public MethodInvokeException() {
    }

    public MethodInvokeException(String message) {
        super(message);
    }

    public MethodInvokeException(String message, Throwable cause) {
        super(message, cause);
    }
}
