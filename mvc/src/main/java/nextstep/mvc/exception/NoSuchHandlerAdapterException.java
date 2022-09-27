package nextstep.mvc.exception;

public class NoSuchHandlerAdapterException extends RuntimeException {

    public NoSuchHandlerAdapterException() {
        super("매칭되는 핸들러 어뎁터가 존재하지 않습니다.");
    }
}
