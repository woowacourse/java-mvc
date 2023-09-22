package webmvc.org.springframework.web.servlet.mvc.exception;

public class HandlerAdapterNotFoundException extends RuntimeException {

    public HandlerAdapterNotFoundException() {
        super("handler에 알맞은 handlerAdapter가 존재하지 않습니다.");
    }
}
