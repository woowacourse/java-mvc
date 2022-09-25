package nextstep.mvc.exception;

public class NotFoundHandlerAdapter extends RuntimeException {

    public NotFoundHandlerAdapter() {
        super("해당 로직을 처리할 HandlerAdapter를 찾을 수 없습니다.");
    }
}
