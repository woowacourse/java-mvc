package nextstep.mvc.exception;

public abstract class ReflectionException extends RuntimeException {

    private static final String REFLECTION_EXCEPTION_MESSAGE = "reflection exception - ";

    protected ReflectionException(final String message) {
        super(REFLECTION_EXCEPTION_MESSAGE + message);
    }
}
