package nextstep.mvc.exception;

public class MethodAccessException extends RuntimeException {

    public MethodAccessException() {
        super("메소드 접근에 실패하였습니다.");
    }
}
