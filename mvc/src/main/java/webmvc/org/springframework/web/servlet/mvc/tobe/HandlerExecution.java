package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.view.ModelAndView;

public class HandlerExecution {

    private final Object controller;
    private final Method method;

    private HandlerExecution(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public static HandlerExecution of(Class<?> controller, Method method) {
        try {
            return new HandlerExecution(controller.getConstructor().newInstance(), method);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(controller, request, response);
    }
}
