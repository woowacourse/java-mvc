package nextstep.mvc.controller.tobe;

import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;

public class HandlerExecution {
    private final Object declaredObject;
    private final Method method;

    public HandlerExecution(Object declaredObject, Method method) {
        this.declaredObject = declaredObject;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object result = method.invoke(declaredObject, request, response);
        if (result instanceof String) {
            JspView jspView = new JspView((String) result);
            return new ModelAndView(jspView);
        }

        return (ModelAndView) result;
    }

    @Override
    public String toString() {
        return "HandlerExecution{" +
                "declaredObject=" + declaredObject +
                ", method=" + method +
                '}';
    }
}
