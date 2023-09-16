package webmvc.org.springframework.web.servlet.mvc.tobe;

public class HandlerExecution {

    private final Object bean;
    private final Object handler;

    public HandlerExecution(final Object bean, final Object handler) {
        this.bean = bean;
        this.handler = handler;
    }

    public Object getHandler() {
        return handler;
    }

    public Object getBean() {
        return bean;
    }
}
