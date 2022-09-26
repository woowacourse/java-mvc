package nextstep.mvc.controller.tobe.exception;

public class NotSupportInstantiateControllerException extends RuntimeException {
    public NotSupportInstantiateControllerException() {
        super("controller 의 instacne 를 만들 수 없습니다.");
    }
}
