package nextstep.mvc.handler;

import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Method method;

    public HandlerExecution(Method method) {
        this.method = method;
    }

    public Object handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object handler = createHandlerInstance(method.getDeclaringClass());
        return method.invoke(handler, request, response);
    }

    private Object createHandlerInstance(Class<?> handler) {
        try {
            return handler.getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
}
