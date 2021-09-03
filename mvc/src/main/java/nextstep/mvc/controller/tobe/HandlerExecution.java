package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Class<?> controllerClass;
    private final Method controllerMethod;

    public HandlerExecution(Class<?> controllerClass, Method controllerMethod) {
        this.controllerClass = controllerClass;
        this.controllerMethod = controllerMethod;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object controller = controllerClass.getConstructor().newInstance();
        return (ModelAndView) controllerMethod.invoke(controller, request, response);
    }
}
