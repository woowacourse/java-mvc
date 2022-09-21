package nextstep.mvc.controller.exception;

public class NoConstructorException extends RuntimeException{

    private static final String MESSAGE = "해당 클래스의 생성자가 존재하지 않습니다";

    public NoConstructorException() {
        super(MESSAGE);
    }
}
