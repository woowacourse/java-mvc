package nextstep.mvc.controller.exception;

public class HandlerMappingInitiationException extends RuntimeException {
    private static final String MESSAGE = "Handler 인스턴스를 생성하는 데 실패했습니다.";

    public HandlerMappingInitiationException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
