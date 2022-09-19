package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {
    private final Class<?> handlerClass;
    private final Method handlerMethod;

    public HandlerExecution(final Class<?> handlerClass, final Method handlerMethod) {
        this.handlerClass = handlerClass;
        this.handlerMethod = handlerMethod;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) handlerMethod.invoke(handlerClass.newInstance(), request, response);
    }
}
