package nextstep.mvc.common.exception;

public class NotFoundHandlerAdapterException extends RuntimeException {

    public NotFoundHandlerAdapterException(final ErrorType errorType) {
        super(errorType.getMessage());
    }
}
