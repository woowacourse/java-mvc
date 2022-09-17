package nextstep.mvc.common.exception;

public class FailedControllerMappingException extends RuntimeException {

    public FailedControllerMappingException(final ErrorType errorType) {
        super(errorType.getMessage());
    }
}
