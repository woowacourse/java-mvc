package nextstep.mvc.exception;

public class NoDefaultConstructorException extends RuntimeException {

    private static final String ERROR_MESSAGE = "No Default Constructor Found";

    public NoDefaultConstructorException() {
        super(ERROR_MESSAGE);
    }
}
