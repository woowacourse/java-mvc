package nextstep.mvc.exception;

public class HandlerAdapterNotFoundException extends MvcServletException {

    public HandlerAdapterNotFoundException() {
        super("요청을 처리하는 핸들러 어답터를 찾지 못했습니다.");
    }
}
