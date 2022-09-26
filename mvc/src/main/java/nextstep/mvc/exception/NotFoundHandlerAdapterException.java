package nextstep.mvc.exception;

public class NotFoundHandlerAdapterException extends RuntimeException{

    public NotFoundHandlerAdapterException() {
        super("해당하는 Handler Adapter를 찾을 수 없습니다.");
    }
}
