package nextstep.mvc.exception;

public class HandlerAdapterNotFoundException extends RuntimeException {

    public HandlerAdapterNotFoundException() {
        super("처리 가능한 HandlerAdapter를 찾을 수 없습니다.");
    }
}
