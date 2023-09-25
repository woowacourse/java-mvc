package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class HandlerExecution {

    private final Method method;
    private final Object instance;

    public HandlerExecution(final Method method) {
        if (isNotReturnType(method) && isNotSameParameter(method)) {
            throw new IllegalArgumentException();
        }
        this.method = method;
        this.instance = newInstance(method);
    }

    private boolean isNotReturnType(final Method method) {
        return !method.getReturnType().equals(ModelAndView.class);
    }

    private boolean isNotSameParameter(final Method method) {
        List<Class<?>> parameterTypes = List.of(method.getParameterTypes());
        return !parameterTypes.containsAll(List.of(HttpServletRequest.class, HttpServletResponse.class));
    }

    private Object newInstance(final Method method) {
        try {
            return method.getDeclaringClass().getConstructor().newInstance();
        } catch (final Exception e) {
            throw new IllegalArgumentException();
        }
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(instance, request, response);
    }
}
