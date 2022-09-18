package nextstep.mvc.controller.tobe.exception;

public class ControllerNotFoundException extends RuntimeException {

    public ControllerNotFoundException() {
        super("요청에 대한 적절한 컨트롤러를 찾을 수 없습니다.");
    }
}
