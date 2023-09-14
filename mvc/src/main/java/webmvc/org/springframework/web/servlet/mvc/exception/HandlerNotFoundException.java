package webmvc.org.springframework.web.servlet.mvc.exception;

public class HandlerNotFoundException extends RuntimeException {

    public HandlerNotFoundException() {
        super("현재 존재하지 않는 Handler입니다.");
    }
}
