package nextstep.web.exception;

public class MethodNotAllowedException extends RuntimeException{

    public MethodNotAllowedException() {
        super("지원하지 않는 메서드입니다.");
    }
}
