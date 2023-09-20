package webmvc.org.springframework.web.servlet.mvc.tobe.handler.exception;

public class HandlerAdapterNotFoundException extends RuntimeException {

    public HandlerAdapterNotFoundException() {
        super("해당 요청을 처리할 수 없습니다.");
    }
}
