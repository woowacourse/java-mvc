package nextstep.mvc.exception;

public class NotFoundHandlerAdapterException extends RuntimeException {

    private static final String MESSAGE = "적합한 핸들러를 찾을 수 없습니다.";

    public NotFoundHandlerAdapterException() {
        super(MESSAGE);
    }
}
