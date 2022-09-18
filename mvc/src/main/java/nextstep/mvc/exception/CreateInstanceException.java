package nextstep.mvc.exception;

public class CreateInstanceException extends RuntimeException {

    public CreateInstanceException() {
        super("인스턴스 생성에 실패하였습니다.");
    }
}
