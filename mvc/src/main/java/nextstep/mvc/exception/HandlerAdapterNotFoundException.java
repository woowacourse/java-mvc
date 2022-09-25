package nextstep.mvc.exception;

public class HandlerAdapterNotFoundException extends RuntimeException {

    public HandlerAdapterNotFoundException() {
        super("지원하는 핸들러 어뎁터가 존재하지 않습니다.");
    }
}
