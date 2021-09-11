package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Object handler;
    private final Method handlerMethod;

    public HandlerExecution(Object handler, Method handlerMethod) {
        this.handler = handler;
        this.handlerMethod = handlerMethod;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response)
        throws InvocationTargetException, IllegalAccessException {
        return (ModelAndView) handlerMethod.invoke(handler, request, response);
    }
}
