package nextstep.mvc.controller.tobe.exception;

public class MethodInvokeException extends RuntimeException {

    public MethodInvokeException() {
        super("메소드를 실행할 수 없습니다.");
    }
}
