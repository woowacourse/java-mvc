package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import nextstep.mvc.exception.ReflectionException;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Object instance;
    private final Method method;

    public HandlerExecution(final Object instance, final Method method) {
        this.instance = instance;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            return (ModelAndView) method.invoke(instance, request, response);
        } catch (final InvocationTargetException | IllegalAccessException e) {
            throw new ReflectionException("Failed to invoke the underlying method.", e);
        }
    }

    @Override
    public String toString() {
        return "Instance : " + instance.getClass() +
                ", Method : " + method.getName();
    }
}
