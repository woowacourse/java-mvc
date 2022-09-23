package nextstep.mvc.exception;

public class HandlerAdapterNotFoundException extends RuntimeException {

    public HandlerAdapterNotFoundException() {
        super("해당하는 HandlerAdapter를 찾을 수 없습니다");
    }
}
