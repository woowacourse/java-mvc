package nextstep.mvc.exception;

public class EmptyModelException extends RuntimeException {
    public EmptyModelException() {
        super("모델이 비어있습니다.");
    }
}
