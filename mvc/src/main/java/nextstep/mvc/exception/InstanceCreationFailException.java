package nextstep.mvc.exception;

public class InstanceCreationFailException extends RuntimeException{
    public InstanceCreationFailException() {
        super("인스턴스를 생성할 수 없습니다.");
    }
}
