package webmvc.org.springframework.web.servlet.mvc.tobe.handler.exception;

public class HandlerMapperNotFoundException extends RuntimeException {

    public HandlerMapperNotFoundException(final String method, final String requestURI) {
        super(method + " " + requestURI + "요청을 처리할 수 없습니다.");
    }
}
