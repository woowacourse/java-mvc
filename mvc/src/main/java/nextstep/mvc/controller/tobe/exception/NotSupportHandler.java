package nextstep.mvc.controller.tobe.exception;

public class NotSupportHandler extends RuntimeException {

    public NotSupportHandler() {
        super("매핑 가능한 핸들러가 존재하지 않습니다.");
    }
}
