package nextstep.mvc.exception;

public class InvalidInvokeMethodException extends ReflectionException {

    private static final String MESSAGE = "유효하지 않은 invoke 요청입니다.";

    public InvalidInvokeMethodException() {
        super(MESSAGE);
    }
}
