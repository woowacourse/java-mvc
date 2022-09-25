package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Method method;
    private final Object handler;

    public HandlerExecution(final Method method, final Object handler) {
        this.method = method;
        this.handler = handler;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Object view = method.invoke(handler, request, response);
        if (view instanceof String) {
            return new ModelAndView(new JspView((String) view));
        }
        return (ModelAndView) view;
    }
}
