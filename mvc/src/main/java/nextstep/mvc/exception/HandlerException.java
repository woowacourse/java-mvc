package nextstep.mvc.exception;

public class HandlerException extends RuntimeException {
    public HandlerException() {
        super("매핑되는 핸들러가 없습니다.");
    }
}
