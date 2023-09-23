package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;

    public HandlerExecution(final Method method) {
        this.method = method;
    }

    public Object handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return method.invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance(), request, response);
    }
}
