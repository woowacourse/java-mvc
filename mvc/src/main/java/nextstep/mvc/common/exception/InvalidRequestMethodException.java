package nextstep.mvc.common.exception;

public class InvalidRequestMethodException extends RuntimeException {

    public InvalidRequestMethodException(final ErrorType errorType) {
        super(errorType.getMessage());
    }
}
