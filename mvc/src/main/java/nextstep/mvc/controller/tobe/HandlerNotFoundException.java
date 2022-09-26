package nextstep.mvc.controller.tobe;

public class HandlerNotFoundException extends RuntimeException {

    private static final String MESSAGE = "핸들러가 존재하지 않습니다.";

    public HandlerNotFoundException() {
        super(MESSAGE);
    }
}
