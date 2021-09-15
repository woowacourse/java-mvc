package nextstep.mvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Object declaredObject;
    private final Method method;

    public HandlerExecution(Object object, Method method) {
        this.declaredObject = object;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        return (ModelAndView) method.invoke(declaredObject, request, response);
    }
}
