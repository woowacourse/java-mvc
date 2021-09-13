package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object instance;
    private final Method method;

    public HandlerExecution(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Object invoke = method.invoke(instance, request, response);
        if (invoke instanceof ModelAndView) {
            return (ModelAndView) invoke;
        }
        return new ModelAndView(new JspView((String) invoke));
    }
}
