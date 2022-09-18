package nextstep.mvc.exception;

public class InvalidMethodException extends RuntimeException {

    private static final String MESSAGE = "존재하지 않거나 접근할 수 없는 메서드입니다.";

    public InvalidMethodException() {
        super(MESSAGE);
    }
}
