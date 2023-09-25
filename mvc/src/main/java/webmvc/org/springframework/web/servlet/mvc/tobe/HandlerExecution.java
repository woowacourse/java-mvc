package webmvc.org.springframework.web.servlet.mvc.tobe;

import core.org.springframework.util.ReflectionUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;

    public HandlerExecution(final Method method) {
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Class<?> declaringClass = method.getDeclaringClass();
        final Constructor<?> constructor = ReflectionUtils.accessibleConstructor(declaringClass);
        final Object instance = constructor.newInstance();
        final Object result = method.invoke(instance, request, response);
        if (result instanceof ModelAndView) {
            return (ModelAndView) result;
        }
        return new ModelAndView(result);
    }
}
