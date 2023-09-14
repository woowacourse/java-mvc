package webmvc.org.springframework.web.servlet.mvc.tobe.exception;

public class HandlerNotFoundException extends RuntimeException {

    public HandlerNotFoundException() {
        super("매핑될 핸들러가 존재하지 않습니다.");
    }
}
