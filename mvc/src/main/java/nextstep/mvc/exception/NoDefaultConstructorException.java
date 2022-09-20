package nextstep.mvc.exception;

public class NoDefaultConstructorException extends RuntimeException {

    private static final String ERROR_MESSAGE = "기본 생성자가 존재하지 않습니다.";

    public NoDefaultConstructorException() {
        super(ERROR_MESSAGE);
    }
}
