package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private Object handler;
    private Method handlerMethod;

    public HandlerExecution(final Object handler, final Method handlerMethod) {
        this.handler = handler;
        this.handlerMethod = handlerMethod;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final ModelAndView modelAndView = (ModelAndView) handlerMethod.invoke(handler, request, response);
        return modelAndView;
    }
}
