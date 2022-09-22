package nextstep.mvc.exception;

public class CreateHandlerInstanceException extends RuntimeException {

    public CreateHandlerInstanceException() {
        super("핸들러 인스턴스 생성에 실패하였습니다.");
    }
}
