package nextstep.mvc.exception;

public class ControllerCreateException extends MvcServletException {

    public ControllerCreateException() {
        super("컨트롤러 인스턴스 생성중 예외가 발생했습니다.");
    }
}
