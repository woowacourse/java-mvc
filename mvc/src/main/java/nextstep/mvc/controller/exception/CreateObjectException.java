package nextstep.mvc.controller.exception;

public class CreateObjectException extends RuntimeException {

    public CreateObjectException() {
        super("인스턴스를 생성 할 수 없습니다.");
    }
}
