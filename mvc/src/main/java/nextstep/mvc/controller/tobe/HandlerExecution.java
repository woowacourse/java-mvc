package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object controller;
    private final Method controllerMethod;

    public HandlerExecution(Object controller, Method controllerMethod) {
        this.controller = controller;
        this.controllerMethod = controllerMethod;
    }

    public Object handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return controllerMethod.invoke(controller, request, response);
    }
}
