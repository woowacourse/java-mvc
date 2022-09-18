package nextstep.mvc.common.exception;

public class FailedHandlerMappingException extends RuntimeException {

    public FailedHandlerMappingException(final ErrorType errorType) {
        super(errorType.getMessage());
    }
}
