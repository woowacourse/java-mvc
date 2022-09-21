package nextstep.mvc.view.exception;

public class NotFoundViewException extends RuntimeException {

    private static final String MESSAGE = "해당 View는 존재하지 않습니다.";

    public NotFoundViewException() {
        super(MESSAGE);
    }
}
