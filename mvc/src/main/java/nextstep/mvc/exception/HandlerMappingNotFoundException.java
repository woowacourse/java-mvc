package nextstep.mvc.exception;

public class HandlerMappingNotFoundException extends IllegalArgumentException {

    private static final String ERROR_MESSAGE = "해당 요청을 처리할 핸들러가 없습니다.";

    public HandlerMappingNotFoundException() {
        super(ERROR_MESSAGE);
    }
}
