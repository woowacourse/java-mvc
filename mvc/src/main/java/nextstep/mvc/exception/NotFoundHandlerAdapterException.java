package nextstep.mvc.exception;

public class NotFoundHandlerAdapterException extends RuntimeException {

    public NotFoundHandlerAdapterException(Object handler) {
        super("해당 요청을 처리할 HandlerAdapter가 존재하지 않습니다. handler: " + handler.getClass().getSimpleName());
    }
}
