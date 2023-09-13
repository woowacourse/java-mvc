package webmvc.org.springframework.web.servlet.mvc.tobe.exception;

public class NotFoundHandlerException extends RuntimeException {

    public NotFoundHandlerException() {
        super("매핑될 핸들러가 존재하지 않습니다.");
    }
}
