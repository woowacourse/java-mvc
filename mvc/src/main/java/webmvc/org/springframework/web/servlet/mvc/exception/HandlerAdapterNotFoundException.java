package webmvc.org.springframework.web.servlet.mvc.exception;

public class HandlerAdapterNotFoundException extends IllegalArgumentException {

    public HandlerAdapterNotFoundException() {
        super("지원하는 Handler Adapter를 찾을 수 없습니다.");
    }
}
