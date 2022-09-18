package nextstep.mvc.common.exception;

public class NotFoundHandlerMappingException extends RuntimeException {

    public NotFoundHandlerMappingException(final ErrorType errorType) {
        super(errorType.getMessage());
    }
}
