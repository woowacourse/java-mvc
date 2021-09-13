package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;
    private final Object controller;

    public HandlerExecution(Method method, Object controller) {
        this.method = method;
        this.controller = controller;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws InvocationTargetException, IllegalAccessException {
        return (ModelAndView) method.invoke(controller, request, response);
    }

    public Object getController() {
        return controller;
    }
}
