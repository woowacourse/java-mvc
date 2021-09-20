package nextstep.mvc.exception;

public class NotSupportedMethod extends RuntimeException {

    public NotSupportedMethod() {
    }

    public NotSupportedMethod(String message) {
        super(message);
    }

    public NotSupportedMethod(String message, Throwable cause) {
        super(message, cause);
    }
}
