package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;

public class HandlerExecution {

    private final Class<?> clazz;
    private final Method method;

    public HandlerExecution(final Class<?> clazz, final Method method) {
        this.clazz = clazz;
        this.method = method;
    }

    public Object handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return method.invoke(clazz.newInstance(), request, response);
    }
}
