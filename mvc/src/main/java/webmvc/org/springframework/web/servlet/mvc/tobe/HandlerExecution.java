package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method handler;
    private final Object instance;

    public HandlerExecution(Method handler, Object instance) {
        this.handler = handler;
        this.instance = instance;
    }

    public Object handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return handler.invoke(instance, request, response);
    }

    public Class<?> getReturnType() {
        return handler.getReturnType();
    }
}
