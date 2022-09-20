package nextstep.mvc.exception;

public class NoHandlerFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "No Handler Found";

    public NoHandlerFoundException(final String httpMethod, final String requestUrl) {
        super(ERROR_MESSAGE + " " + httpMethod + " " + requestUrl);
    }
}
