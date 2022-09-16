package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Class<?> controllerType;
    private final Method method;

    public HandlerExecution(Class<?> controllerType, Method method) {
        this.controllerType = controllerType;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(controllerType.getConstructor().newInstance(), request, response);
    }
}
