package nextstep.mvc.controller.exception;

public class InstantiatingFailedException extends RuntimeException {

    public InstantiatingFailedException() {
        super("인스턴스화에 실패했습니다.");
    }
}
