package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Object handler;
    private final Method method;

    public HandlerExecution(final Object handler, final Method method) {
        this.handler = handler;
        this.method = method;
    }

    public Object handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Object invoke = method.invoke(handler, request, response);
        if (invoke instanceof ModelAndView) {
            return invoke;
        }
        return new ModelAndView(new JspView((String) invoke));
    }
}
