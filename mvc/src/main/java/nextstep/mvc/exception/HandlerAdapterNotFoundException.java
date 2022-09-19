package nextstep.mvc.exception;

public class HandlerAdapterNotFoundException extends RuntimeException {

    private static final String MESSAGE = "적절한 Handler Adapter가 존재하지 않습니다.";

    public HandlerAdapterNotFoundException() {
        super(MESSAGE);
    }
}
