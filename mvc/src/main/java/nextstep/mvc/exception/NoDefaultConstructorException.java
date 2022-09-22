package nextstep.mvc.exception;

public class NoDefaultConstructorException extends ReflectionException {

    private static final String MESSAGE = "사용하려는 기본 생성자가 존재하지 않습니다.";

    public NoDefaultConstructorException() {
        super(MESSAGE);
    }
}
