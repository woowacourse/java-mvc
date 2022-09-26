package nextstep.mvc.controller.exception;

public class ControllerInstantiationException extends RuntimeException {

    private static final String ERROR_MESSAGE = "컨트롤러를 인스턴스화 하지 못하였습니다.";

    public ControllerInstantiationException() {
        super(ERROR_MESSAGE);
    }
}
