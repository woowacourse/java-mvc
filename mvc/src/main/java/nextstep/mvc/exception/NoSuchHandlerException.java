package nextstep.mvc.exception;

public class NoSuchHandlerException extends RuntimeException {

    public NoSuchHandlerException() {
        super("매칭되는 핸들러가 존재하지 않습니다.");
    }
}
