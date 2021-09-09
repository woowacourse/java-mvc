package nextstep.mvc.controller.tobe.handler.mapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.exception.controller.IllegalMethodException;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Object handler;
    private final Method method;

    public HandlerExecution(Object handler, Method method) {
        this.handler = handler;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
        try {
            return (ModelAndView) method.invoke(handler, request, response);
        } catch (Exception e) {
            throw new IllegalMethodException();
        }
    }
}
