package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution {
    private final Method method;
       private final Object controller;
    public HandlerExecution(Method method) {
        this.method = method;
        try {
            this.controller = method.getDeclaringClass().getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(controller, request, response);
    }
}
