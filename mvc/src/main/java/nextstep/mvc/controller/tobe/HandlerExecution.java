package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Class<?> handler;
    private final Method method;

    public HandlerExecution(final Class<?> handler, final Method method) {
        this.handler = handler;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object instance = handler.getConstructor().newInstance();
        return (ModelAndView) method.invoke(instance, request, response);
    }
}
