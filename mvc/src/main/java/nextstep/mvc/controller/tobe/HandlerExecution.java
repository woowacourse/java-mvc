package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method handlerMethod;
    private final Object controller;

    public HandlerExecution(Method handlerMethod, Object controller) {
        this.handlerMethod = handlerMethod;
        this.controller = controller;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object result = handlerMethod.invoke(controller, request, response);
        if (result instanceof String) {
            String viewName = (String) result;
            return new ModelAndView(new JspView(viewName));
        }
        return (ModelAndView) result;
    }
}
