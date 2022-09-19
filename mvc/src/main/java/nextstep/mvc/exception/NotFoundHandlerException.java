package nextstep.mvc.exception;

public class NotFoundHandlerException extends RuntimeException {

    public NotFoundHandlerException() {
        super("해당하는 Handler를 찾을 수 없습니다.");
    }
}
