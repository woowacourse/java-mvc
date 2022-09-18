package nextstep.mvc.controller.exception;

public class InitializingFailedException extends RuntimeException {

    public InitializingFailedException() {
        super("초기화에 실패했습니다.");
    }
}
