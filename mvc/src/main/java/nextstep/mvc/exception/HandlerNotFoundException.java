package nextstep.mvc.exception;

public class HandlerNotFoundException extends MvcException {

    public HandlerNotFoundException(String uri) {
        super(String.format("매핑된 핸들러가 없습니다. (%s)", uri));
    }
}
