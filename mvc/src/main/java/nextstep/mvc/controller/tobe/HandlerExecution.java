package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Object controller;
    private final Method method;

    public HandlerExecution(final Class<?> controllerClass, final Method method) {
        try {
            Constructor<?> constructor = controllerClass.getConstructor();

            this.controller = constructor.newInstance();
            this.method = method;
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(controller, request, response);
    }
}
