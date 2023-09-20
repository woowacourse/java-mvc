package webmvc.org.springframework.web.servlet.mvc.tobe;

public class HandlerExecution {

    private final Object bean;
    private final Object method;

    public HandlerExecution(final Object bean, final Object method) {
        this.bean = bean;
        this.method = method;
    }

    public Object getMethod() {
        return method;
    }

    public Object getBean() {
        return bean;
    }
}
