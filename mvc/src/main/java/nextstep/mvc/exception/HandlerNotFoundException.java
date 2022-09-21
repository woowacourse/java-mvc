package nextstep.mvc.exception;

public class HandlerNotFoundException extends MvcServletException {

    public HandlerNotFoundException() {
        super("요청을 처리하는 핸들러를 찾지 못했습니다.");
    }
}
