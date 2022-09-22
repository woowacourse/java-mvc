package nextstep.mvc.controller.exception;

public class NotFoundHandlerException extends RuntimeException {
    public NotFoundHandlerException(final String url, final String method) {
        super("핸들러를 찾을 수 없습니다. url : " + url + " method : " + method);
    }
}
