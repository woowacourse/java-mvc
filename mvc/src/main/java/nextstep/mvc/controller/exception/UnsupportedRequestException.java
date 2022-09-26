package nextstep.mvc.controller.exception;

public class UnsupportedRequestException extends RuntimeException {

    public UnsupportedRequestException() {
        super("지원되지 않는 요청입니다.");
    }
}
