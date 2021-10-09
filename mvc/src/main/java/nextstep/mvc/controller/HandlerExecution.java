package nextstep.mvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Method method;
    private final Object instance;

    public HandlerExecution(Method method, Object instance) {
        this.method = method;
        this.instance = instance;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return (ModelAndView) method.invoke(instance, request, response);
    }
}
