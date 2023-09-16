package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class HandlerExecution {

    private final Method method;

    public HandlerExecution(final Method method) {
        this.method = method;
    }

    private Object newInstance() {
        try {
            return method.getDeclaringClass().getConstructor().newInstance();
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(newInstance(), request, response);
    }
}
