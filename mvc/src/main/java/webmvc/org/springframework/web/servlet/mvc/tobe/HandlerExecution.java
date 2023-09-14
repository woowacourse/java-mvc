package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class HandlerExecution {

    private Object controller;
    private Method method;

    private HandlerExecution() {
    }

    public HandlerExecution(final Method method, final Object controller) {
        this.controller = controller;
        this.method = method;
    }

    public static HandlerExecution empty() {
        return new HandlerExecution();
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(controller, request, response);
    }
}
