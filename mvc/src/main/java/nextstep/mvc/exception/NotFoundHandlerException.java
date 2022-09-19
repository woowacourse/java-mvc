package nextstep.mvc.exception;

public class NotFoundHandlerException extends RuntimeException {

    private static final String MESSAGE = "적합한 핸들러를 찾을 수 없습니다.";

    public NotFoundHandlerException() {
        super(MESSAGE);
    }
}
