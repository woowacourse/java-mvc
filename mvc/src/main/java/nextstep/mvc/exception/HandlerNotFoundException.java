package nextstep.mvc.exception;

public class HandlerNotFoundException extends RuntimeException {

    private static final String MESSAGE = "적절한 Handler가 존재하지 않습니다.";

    public HandlerNotFoundException() {
        super(MESSAGE);
    }
}
