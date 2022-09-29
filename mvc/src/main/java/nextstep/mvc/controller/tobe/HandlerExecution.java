package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Method method;
    private final Object instance;

    public HandlerExecution(Method method, Object instance) {
        checkSameInstance(method, instance);
        this.method = method;
        this.instance = instance;
    }

    private void checkSameInstance(Method method, Object instance) {
        if (!method.getDeclaringClass().isInstance(instance)) {
            throw new IllegalArgumentException("Instance must contain method");
        }
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(instance, request, response);
    }
}
