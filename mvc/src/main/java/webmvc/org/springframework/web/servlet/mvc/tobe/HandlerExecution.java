package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Class<?> controller;
    private final Method handler;

    public HandlerExecution(final Class<?> controller, final Method handler) {
        this.controller = controller;
        this.handler = handler;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        return (ModelAndView) handler.invoke(controller.newInstance(), request, response);
    }
}
