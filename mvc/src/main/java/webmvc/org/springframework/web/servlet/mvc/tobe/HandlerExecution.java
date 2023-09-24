package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object instance;
    private final Method method;

    public HandlerExecution(final Object instance, final Method method) {
        this.instance = instance;
        this.method = method;
    }

    public Object handle(final HttpServletRequest request, final HttpServletResponse response)
            throws InvocationTargetException, IllegalAccessException {
        return method.invoke(instance, request, response);
    }
}
