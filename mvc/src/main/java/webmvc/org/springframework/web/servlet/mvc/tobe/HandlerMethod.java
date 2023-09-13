package webmvc.org.springframework.web.servlet.mvc.tobe;

import java.lang.reflect.Method;

public class HandlerMethod {

    private final Object handler;
    private final Method method;

    public HandlerMethod(Object handler, Method method) {
        this.handler = handler;
        this.method = method;
    }

    public Object getBean() {
        return this.handler;
    }

    public Method getMethod() {
        return this.method;
    }

    public Class<?> getBeanType() {
        return this.handler.getClass();
    }
}
