package nextstep.core.exception;

public class BeanException extends RuntimeException{

    public BeanException(BeanErrorMessage message) {
        super(message.getMessage());
    }
}
