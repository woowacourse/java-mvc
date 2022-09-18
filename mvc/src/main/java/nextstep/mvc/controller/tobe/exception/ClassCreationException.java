package nextstep.mvc.controller.tobe.exception;

public class ClassCreationException extends RuntimeException {

    public ClassCreationException() {
        super("요청된 클래스명과 일치하는 클래스를 찾을 수 없습니다.");
    }
}
