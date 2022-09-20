package nextstep.mvc.exception;

public class FailMapHandler extends RuntimeException {

    public FailMapHandler() {
        super("매핑 중 오류가 발생하였습니다.");
    }
}
