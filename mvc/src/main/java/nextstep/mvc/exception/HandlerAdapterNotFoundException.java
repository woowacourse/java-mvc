package nextstep.mvc.exception;

public class HandlerAdapterNotFoundException extends RuntimeException {

    private static final String MESSAGE = "적절한 핸들러를 찾을 수 없습니다.";

    public HandlerAdapterNotFoundException() {
        super(MESSAGE);
    }
}
