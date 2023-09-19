package webmvc.org.springframework.web.servlet.mvc.tobe.exception;

public class HandlerAdapterNotFoundException extends RuntimeException {

    public HandlerAdapterNotFoundException() {
        super("Handler를 지원하는 HandlerAdapter를 찾을 수 없습니다.");
    }
}
