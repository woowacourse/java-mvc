package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution<T> {

    private final T controller;
    private final Method method;

    public HandlerExecution(T controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        return (ModelAndView) method.invoke(controller, request, response);
    }
}
