package webmvc.org.springframework.web.servlet.mvc.tobe;

import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class HandlerExecution {
    private final Method method;
    private final Object controller;

    public HandlerExecution(final Method method, final Object controller) {
        this.method = method;
        this.controller = controller;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(controller, request, response);
    }
}
