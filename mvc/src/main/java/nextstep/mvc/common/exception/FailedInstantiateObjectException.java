package nextstep.mvc.common.exception;

public class FailedInstantiateObjectException extends RuntimeException {

    public FailedInstantiateObjectException(final ErrorType errorType) {
        super(errorType.getMessage());
    }
}
