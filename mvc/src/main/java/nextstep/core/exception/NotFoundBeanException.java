package nextstep.core.exception;

public class NotFoundBeanException extends RuntimeException{

    private static final String MESSAGE = "해당하는 빈을 찾을 수 없습니다.";

    public NotFoundBeanException() {
        super(MESSAGE);
    }
}
