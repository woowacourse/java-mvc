package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object controller;
    private final Method method;

    public HandlerExecution(final Method method, final Object controller) {
        this.controller = controller;
        this.method = method;
    }

    public Object handle(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws IllegalAccessException, InvocationTargetException {
        return method.invoke(controller, request, response);
    }

    public Method method() {
        return method;
    }
}
