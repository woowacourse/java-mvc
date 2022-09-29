package nextstep.mvc.exception;

public class EmptyModelException extends RuntimeException {

    public EmptyModelException() {
        super("Model에 attribute 값이 들어있지 않습니다.");
    }
}
