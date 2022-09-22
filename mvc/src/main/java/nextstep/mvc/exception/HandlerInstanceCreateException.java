package nextstep.mvc.exception;

public class HandlerInstanceCreateException extends RuntimeException {

    private static final String MESSAGE = "인스턴스를 생성할 수 없습니다.";

    public HandlerInstanceCreateException() {
        super(MESSAGE);
    }
}
