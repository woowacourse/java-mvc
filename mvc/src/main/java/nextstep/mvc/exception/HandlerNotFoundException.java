package nextstep.mvc.exception;

public class HandlerNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Handler Not Found";

    public HandlerNotFoundException() {
        super(MESSAGE);
    }
}
