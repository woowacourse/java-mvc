package nextstep.mvc.exception;

public class HandlerNotFoundException extends RuntimeException {

    public HandlerNotFoundException() {
        super("처리 가능한 Handler를 찾을 수 없습니다.");
    }
}
