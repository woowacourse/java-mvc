package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Object controller;
    private final Method controllerMethod;

    public HandlerExecution(Object controller, Method controllerMethod) {
        this.controller = controller;
        this.controllerMethod = controllerMethod;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) controllerMethod.invoke(controller, request, response);
    }
}
