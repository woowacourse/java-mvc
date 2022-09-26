package nextstep.mvc.exception;

public class HandlerMappingNotFoundException extends RuntimeException {
    public HandlerMappingNotFoundException(final String message) {
        super(message);
    }
}
