package nextstep.core.exception;

public class BeanException extends RuntimeException{

    private static final String MESSAGE = "해당하는 빈을 찾을 수 없습니다.";

    public BeanException(BeanErrorMessage message) {
        super(message.getMessage());
    }
}
