package webmvc.org.springframework.web.servlet.mvc.tobe;

import core.org.springframework.util.ReflectionUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerExecutionException;

public class HandlerExecution {

    private final Object controller;
    private final Method method;

    private HandlerExecution(final Method method, final Object controller) {
        this.method = method;
        this.controller = controller;
    }

    public static HandlerExecution from(final Method method) {
        try {
            final Object controller = getControllerToRunMethod(method);

            return new HandlerExecution(method, controller);
        } catch (Exception e) {
            throw new HandlerExecutionException.InitializationException();
        }
    }

    private static Object getControllerToRunMethod(final Method method) throws Exception {
        final Class<?> clazz = method.getDeclaringClass();

        return ReflectionUtils.accessibleConstructor(clazz).newInstance();
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response)
        throws Exception
    {
        return (ModelAndView) method.invoke(controller, request, response);
    }
}
