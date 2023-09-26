package webmvc.org.springframework.web.servlet.mvc.tobe.handler_mapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.exception.HandlerExecutionNotInitializeException;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.Handler;

public class HandlerExecution implements Handler {

    private final Method method;
    private final Object object;

    public HandlerExecution(final Method method) {
        this.method = method;
        try {
            this.object = method.getDeclaringClass().getConstructor().newInstance();
        } catch (final Exception e) {
            throw new HandlerExecutionNotInitializeException();
        }
    }

    @Override
    public final ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response)
        throws Exception {
        return (ModelAndView) method.invoke(object, request, response);
    }
}
