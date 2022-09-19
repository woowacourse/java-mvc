package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Object targetInstance;
    private final Method targetMethod;

    public HandlerExecution(final Object targetInstance, final Method targetMethod) {
        this.targetInstance = targetInstance;
        this.targetMethod = targetMethod;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            return (ModelAndView) targetMethod.invoke(targetInstance, request, response);
        } catch (final InvocationTargetException | IllegalAccessException e) {
            throw new IllegalArgumentException("Reflection: Failed to invoke the underlying method.");
        }
    }
}
