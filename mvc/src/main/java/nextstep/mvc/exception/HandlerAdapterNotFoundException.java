package nextstep.mvc.exception;

public class HandlerAdapterNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Handler Adapter Not Found";

    public HandlerAdapterNotFoundException() {
        super(MESSAGE);
    }
}
