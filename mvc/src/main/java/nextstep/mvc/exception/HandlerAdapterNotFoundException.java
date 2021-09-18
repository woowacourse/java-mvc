package nextstep.mvc.exception;

public class HandlerAdapterNotFoundException extends IllegalArgumentException {

    private static final String ERROR_MESSAGE = "해당 요청을 처리할 어댑터가 없습니다.";

    public HandlerAdapterNotFoundException() {
        super(ERROR_MESSAGE);
    }
}
