package nextstep.mvc.exception;

public class HandlerNotFoundException extends RuntimeException {

    public HandlerNotFoundException() {
        super("핸들러를 찾을 수 없습니다.");
    }
}
