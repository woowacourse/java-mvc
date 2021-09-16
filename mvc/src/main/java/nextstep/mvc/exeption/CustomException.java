package nextstep.mvc.exeption;

public abstract class CustomException extends RuntimeException {

    protected CustomException(String message) {
        super(message);
    }
}
