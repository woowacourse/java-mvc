package nextstep.mvc.controller.tobe.exception;

public class NotFoundControllerException extends RuntimeException {

    public NotFoundControllerException() {
        super("컨트롤러를 찾을 수 없습니다.");
    }
}
