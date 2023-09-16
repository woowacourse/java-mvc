package webmvc.org.springframework.web.servlet.mvc.tobe;

public class HandlerExecution {

    private final Object handler;

    public HandlerExecution(Object handler) {
        this.handler = handler;
    }

    public Object getHandler() {
        return handler;
    }
}
