package nextstep.mvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Object controller;
    private final Method method;

    public HandlerExecution(final Object controller, final Method method) {
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Object result = method.invoke(controller, request, response);
        if (result instanceof ModelAndView) {
            return (ModelAndView) result;
        }
        final String viewName = (String) result;
        return new ModelAndView(new JspView(viewName));
    }
}
