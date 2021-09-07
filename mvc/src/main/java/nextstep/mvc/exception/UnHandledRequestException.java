package nextstep.mvc.exception;

public class UnHandledRequestException extends RuntimeException {

    public UnHandledRequestException() {
    }

    public UnHandledRequestException(String message) {
        super(message);
    }
}
