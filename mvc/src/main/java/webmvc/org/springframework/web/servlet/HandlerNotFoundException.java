package webmvc.org.springframework.web.servlet;

public class HandlerNotFoundException extends RuntimeException {

    public HandlerNotFoundException() {
        super("처리 가능한 Handler가 존재하지 않습니다.");
    }
}
