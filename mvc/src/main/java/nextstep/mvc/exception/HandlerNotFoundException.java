package nextstep.mvc.exception;

public class HandlerNotFoundException extends RuntimeException {

    private static final String MESSAGE = "요청을 처리할 handler가 존재하지 않습니다.";

    public HandlerNotFoundException() {
        super(MESSAGE);
    }
}
