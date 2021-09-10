package nextstep.mvc.exception;

public class DuplicatedRequestMappingException extends RuntimeException {
    public DuplicatedRequestMappingException() {
    }

    public DuplicatedRequestMappingException(String message) {
        super(message);
    }
}
