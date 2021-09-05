package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {
    private Object controller;
    private Method method;

    public HandlerExecution(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws InvocationTargetException, IllegalAccessException {
        return (ModelAndView) method.invoke(controller, request, response);
    }
}
