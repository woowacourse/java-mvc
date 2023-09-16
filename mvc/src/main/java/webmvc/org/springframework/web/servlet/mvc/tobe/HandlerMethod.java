package webmvc.org.springframework.web.servlet.mvc.tobe;

public class HandlerMethod {

    private final Object bean;
    private final Object handler;

    public HandlerMethod(Object bean, Object handler) {
        this.bean = bean;
        this.handler = handler;
    }

    public Object getBean() {
        return bean;
    }

    public Object getHandler() {
        return handler;
    }
}
