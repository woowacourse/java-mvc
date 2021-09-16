package nextstep.mvc.exception;

public class NoSuchRequestMappingException extends RuntimeException {

    public NoSuchRequestMappingException() {
    }

    public NoSuchRequestMappingException(String message) {
        super(message);
    }

    public NoSuchRequestMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}
