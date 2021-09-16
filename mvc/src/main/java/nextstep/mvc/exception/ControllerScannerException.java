package nextstep.mvc.exception;

public class ControllerScannerException extends RuntimeException {

    public ControllerScannerException() {
    }

    public ControllerScannerException(String message) {
        super(message);
    }

    public ControllerScannerException(String message, Throwable cause) {
        super(message, cause);
    }
}
